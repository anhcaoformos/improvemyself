/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentMethodUpdate from './payment-method-update.vue';
import PaymentMethodService from './payment-method.service';
import AlertService from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';

type PaymentMethodUpdateComponentType = InstanceType<typeof PaymentMethodUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentMethodSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentMethodUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PaymentMethod Management Update Component', () => {
    let comp: PaymentMethodUpdateComponentType;
    let paymentMethodServiceStub: SinonStubbedInstance<PaymentMethodService>;

    beforeEach(() => {
      route = {};
      paymentMethodServiceStub = sinon.createStubInstance<PaymentMethodService>(PaymentMethodService);
      paymentMethodServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentMethodService: () => paymentMethodServiceStub,
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
        const wrapper = shallowMount(PaymentMethodUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentMethod = paymentMethodSample;
        paymentMethodServiceStub.update.resolves(paymentMethodSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.update.calledWith(paymentMethodSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentMethodServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentMethodUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentMethod = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentMethodServiceStub.find.resolves(paymentMethodSample);
        paymentMethodServiceStub.retrieve.resolves([paymentMethodSample]);

        // WHEN
        route = {
          params: {
            paymentMethodId: '' + paymentMethodSample.id,
          },
        };
        const wrapper = shallowMount(PaymentMethodUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paymentMethod).toMatchObject(paymentMethodSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentMethodServiceStub.find.resolves(paymentMethodSample);
        const wrapper = shallowMount(PaymentMethodUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
