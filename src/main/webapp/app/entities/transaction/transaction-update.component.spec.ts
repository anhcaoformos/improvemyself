/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TransactionUpdate from './transaction-update.vue';
import TransactionService from './transaction.service';
import AlertService from '@/shared/alert/alert.service';

import ObjectiveService from '@/entities/objective/objective.service';
import PaymentMethodService from '@/entities/payment-method/payment-method.service';
import PaymentCategoryService from '@/entities/payment-category/payment-category.service';
import LedgerService from '@/entities/ledger/ledger.service';

type TransactionUpdateComponentType = InstanceType<typeof TransactionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const transactionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TransactionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Transaction Management Update Component', () => {
    let comp: TransactionUpdateComponentType;
    let transactionServiceStub: SinonStubbedInstance<TransactionService>;

    beforeEach(() => {
      route = {};
      transactionServiceStub = sinon.createStubInstance<TransactionService>(TransactionService);
      transactionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          transactionService: () => transactionServiceStub,
          objectiveService: () =>
            sinon.createStubInstance<ObjectiveService>(ObjectiveService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          paymentMethodService: () =>
            sinon.createStubInstance<PaymentMethodService>(PaymentMethodService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          paymentCategoryService: () =>
            sinon.createStubInstance<PaymentCategoryService>(PaymentCategoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          ledgerService: () =>
            sinon.createStubInstance<LedgerService>(LedgerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TransactionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.transaction = transactionSample;
        transactionServiceStub.update.resolves(transactionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(transactionServiceStub.update.calledWith(transactionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        transactionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TransactionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.transaction = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(transactionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        transactionServiceStub.find.resolves(transactionSample);
        transactionServiceStub.retrieve.resolves([transactionSample]);

        // WHEN
        route = {
          params: {
            transactionId: '' + transactionSample.id,
          },
        };
        const wrapper = shallowMount(TransactionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.transaction).toMatchObject(transactionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        transactionServiceStub.find.resolves(transactionSample);
        const wrapper = shallowMount(TransactionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
