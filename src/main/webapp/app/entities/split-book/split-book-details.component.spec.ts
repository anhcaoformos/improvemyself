/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookDetails from './split-book-details.vue';
import SplitBookService from './split-book.service';
import AlertService from '@/shared/alert/alert.service';

type SplitBookDetailsComponentType = InstanceType<typeof SplitBookDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SplitBook Management Detail Component', () => {
    let splitBookServiceStub: SinonStubbedInstance<SplitBookService>;
    let mountOptions: MountingOptions<SplitBookDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      splitBookServiceStub = sinon.createStubInstance<SplitBookService>(SplitBookService);

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
          splitBookService: () => splitBookServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        splitBookServiceStub.find.resolves(splitBookSample);
        route = {
          params: {
            splitBookId: '' + 123,
          },
        };
        const wrapper = shallowMount(SplitBookDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.splitBook).toMatchObject(splitBookSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookServiceStub.find.resolves(splitBookSample);
        const wrapper = shallowMount(SplitBookDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
