/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentCategoryUpdate from './payment-category-update.vue';
import PaymentCategoryService from './payment-category.service';
import AlertService from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';

type PaymentCategoryUpdateComponentType = InstanceType<typeof PaymentCategoryUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentCategorySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentCategoryUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PaymentCategory Management Update Component', () => {
    let comp: PaymentCategoryUpdateComponentType;
    let paymentCategoryServiceStub: SinonStubbedInstance<PaymentCategoryService>;

    beforeEach(() => {
      route = {};
      paymentCategoryServiceStub = sinon.createStubInstance<PaymentCategoryService>(PaymentCategoryService);
      paymentCategoryServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentCategoryService: () => paymentCategoryServiceStub,
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
        const wrapper = shallowMount(PaymentCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentCategory = paymentCategorySample;
        paymentCategoryServiceStub.update.resolves(paymentCategorySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.update.calledWith(paymentCategorySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentCategoryServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentCategory = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentCategoryServiceStub.find.resolves(paymentCategorySample);
        paymentCategoryServiceStub.retrieve.resolves([paymentCategorySample]);

        // WHEN
        route = {
          params: {
            paymentCategoryId: '' + paymentCategorySample.id,
          },
        };
        const wrapper = shallowMount(PaymentCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paymentCategory).toMatchObject(paymentCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentCategoryServiceStub.find.resolves(paymentCategorySample);
        const wrapper = shallowMount(PaymentCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
