/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookJoinerDetails from './split-book-joiner-details.vue';
import SplitBookJoinerService from './split-book-joiner.service';
import AlertService from '@/shared/alert/alert.service';

type SplitBookJoinerDetailsComponentType = InstanceType<typeof SplitBookJoinerDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookJoinerSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SplitBookJoiner Management Detail Component', () => {
    let splitBookJoinerServiceStub: SinonStubbedInstance<SplitBookJoinerService>;
    let mountOptions: MountingOptions<SplitBookJoinerDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      splitBookJoinerServiceStub = sinon.createStubInstance<SplitBookJoinerService>(SplitBookJoinerService);

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
          splitBookJoinerService: () => splitBookJoinerServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        splitBookJoinerServiceStub.find.resolves(splitBookJoinerSample);
        route = {
          params: {
            splitBookJoinerId: '' + 123,
          },
        };
        const wrapper = shallowMount(SplitBookJoinerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.splitBookJoiner).toMatchObject(splitBookJoinerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookJoinerServiceStub.find.resolves(splitBookJoinerSample);
        const wrapper = shallowMount(SplitBookJoinerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
