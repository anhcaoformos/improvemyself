/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TransactionDetails from './transaction-details.vue';
import TransactionService from './transaction.service';
import AlertService from '@/shared/alert/alert.service';

type TransactionDetailsComponentType = InstanceType<typeof TransactionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const transactionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Transaction Management Detail Component', () => {
    let transactionServiceStub: SinonStubbedInstance<TransactionService>;
    let mountOptions: MountingOptions<TransactionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      transactionServiceStub = sinon.createStubInstance<TransactionService>(TransactionService);

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
          transactionService: () => transactionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        transactionServiceStub.find.resolves(transactionSample);
        route = {
          params: {
            transactionId: '' + 123,
          },
        };
        const wrapper = shallowMount(TransactionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.transaction).toMatchObject(transactionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        transactionServiceStub.find.resolves(transactionSample);
        const wrapper = shallowMount(TransactionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
