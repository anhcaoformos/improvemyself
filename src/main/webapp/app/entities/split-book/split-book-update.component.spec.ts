/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookUpdate from './split-book-update.vue';
import SplitBookService from './split-book.service';
import AlertService from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';

type SplitBookUpdateComponentType = InstanceType<typeof SplitBookUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SplitBookUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SplitBook Management Update Component', () => {
    let comp: SplitBookUpdateComponentType;
    let splitBookServiceStub: SinonStubbedInstance<SplitBookService>;

    beforeEach(() => {
      route = {};
      splitBookServiceStub = sinon.createStubInstance<SplitBookService>(SplitBookService);
      splitBookServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          splitBookService: () => splitBookServiceStub,
          applicationUserService: () =>
            sinon.createStubInstance<ApplicationUserService>(ApplicationUserService, {
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
        const wrapper = shallowMount(SplitBookUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBook = splitBookSample;
        splitBookServiceStub.update.resolves(splitBookSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookServiceStub.update.calledWith(splitBookSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        splitBookServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SplitBookUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBook = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        splitBookServiceStub.find.resolves(splitBookSample);
        splitBookServiceStub.retrieve.resolves([splitBookSample]);

        // WHEN
        route = {
          params: {
            splitBookId: '' + splitBookSample.id,
          },
        };
        const wrapper = shallowMount(SplitBookUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.splitBook).toMatchObject(splitBookSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookServiceStub.find.resolves(splitBookSample);
        const wrapper = shallowMount(SplitBookUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
