/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentCategoryDetails from './payment-category-details.vue';
import PaymentCategoryService from './payment-category.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentCategoryDetailsComponentType = InstanceType<typeof PaymentCategoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentCategorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PaymentCategory Management Detail Component', () => {
    let paymentCategoryServiceStub: SinonStubbedInstance<PaymentCategoryService>;
    let mountOptions: MountingOptions<PaymentCategoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      paymentCategoryServiceStub = sinon.createStubInstance<PaymentCategoryService>(PaymentCategoryService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          paymentCategoryService: () => paymentCategoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentCategoryServiceStub.find.resolves(paymentCategorySample);
        route = {
          params: {
            paymentCategoryId: '' + 123,
          },
        };
        const wrapper = shallowMount(PaymentCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.paymentCategory).toMatchObject(paymentCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentCategoryServiceStub.find.resolves(paymentCategorySample);
        const wrapper = shallowMount(PaymentCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
