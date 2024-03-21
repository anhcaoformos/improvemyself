/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ObjectiveDetails from './objective-details.vue';
import ObjectiveService from './objective.service';
import AlertService from '@/shared/alert/alert.service';

type ObjectiveDetailsComponentType = InstanceType<typeof ObjectiveDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const objectiveSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Objective Management Detail Component', () => {
    let objectiveServiceStub: SinonStubbedInstance<ObjectiveService>;
    let mountOptions: MountingOptions<ObjectiveDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      objectiveServiceStub = sinon.createStubInstance<ObjectiveService>(ObjectiveService);

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
          objectiveService: () => objectiveServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        objectiveServiceStub.find.resolves(objectiveSample);
        route = {
          params: {
            objectiveId: '' + 123,
          },
        };
        const wrapper = shallowMount(ObjectiveDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.objective).toMatchObject(objectiveSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        objectiveServiceStub.find.resolves(objectiveSample);
        const wrapper = shallowMount(ObjectiveDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
