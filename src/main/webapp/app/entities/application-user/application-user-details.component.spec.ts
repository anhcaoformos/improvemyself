/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationUserDetails from './application-user-details.vue';
import ApplicationUserService from './application-user.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationUserDetailsComponentType = InstanceType<typeof ApplicationUserDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationUserSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ApplicationUser Management Detail Component', () => {
    let applicationUserServiceStub: SinonStubbedInstance<ApplicationUserService>;
    let mountOptions: MountingOptions<ApplicationUserDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      applicationUserServiceStub = sinon.createStubInstance<ApplicationUserService>(ApplicationUserService);

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
          applicationUserService: () => applicationUserServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationUserServiceStub.find.resolves(applicationUserSample);
        route = {
          params: {
            applicationUserId: '' + 123,
          },
        };
        const wrapper = shallowMount(ApplicationUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.applicationUser).toMatchObject(applicationUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationUserServiceStub.find.resolves(applicationUserSample);
        const wrapper = shallowMount(ApplicationUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
