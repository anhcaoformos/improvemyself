/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationUserUpdate from './application-user-update.vue';
import ApplicationUserService from './application-user.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationUserUpdateComponentType = InstanceType<typeof ApplicationUserUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationUserSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApplicationUserUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApplicationUser Management Update Component', () => {
    let comp: ApplicationUserUpdateComponentType;
    let applicationUserServiceStub: SinonStubbedInstance<ApplicationUserService>;

    beforeEach(() => {
      route = {};
      applicationUserServiceStub = sinon.createStubInstance<ApplicationUserService>(ApplicationUserService);
      applicationUserServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          applicationUserService: () => applicationUserServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ApplicationUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationUser = applicationUserSample;
        applicationUserServiceStub.update.resolves(applicationUserSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationUserServiceStub.update.calledWith(applicationUserSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        applicationUserServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ApplicationUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationUser = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationUserServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        applicationUserServiceStub.find.resolves(applicationUserSample);
        applicationUserServiceStub.retrieve.resolves([applicationUserSample]);

        // WHEN
        route = {
          params: {
            applicationUserId: '' + applicationUserSample.id,
          },
        };
        const wrapper = shallowMount(ApplicationUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.applicationUser).toMatchObject(applicationUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationUserServiceStub.find.resolves(applicationUserSample);
        const wrapper = shallowMount(ApplicationUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
