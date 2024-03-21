/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookDetailDetails from './split-book-detail-details.vue';
import SplitBookDetailService from './split-book-detail.service';
import AlertService from '@/shared/alert/alert.service';

type SplitBookDetailDetailsComponentType = InstanceType<typeof SplitBookDetailDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookDetailSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SplitBookDetail Management Detail Component', () => {
    let splitBookDetailServiceStub: SinonStubbedInstance<SplitBookDetailService>;
    let mountOptions: MountingOptions<SplitBookDetailDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      splitBookDetailServiceStub = sinon.createStubInstance<SplitBookDetailService>(SplitBookDetailService);

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
          splitBookDetailService: () => splitBookDetailServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        splitBookDetailServiceStub.find.resolves(splitBookDetailSample);
        route = {
          params: {
            splitBookDetailId: '' + 123,
          },
        };
        const wrapper = shallowMount(SplitBookDetailDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.splitBookDetail).toMatchObject(splitBookDetailSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookDetailServiceStub.find.resolves(splitBookDetailSample);
        const wrapper = shallowMount(SplitBookDetailDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
