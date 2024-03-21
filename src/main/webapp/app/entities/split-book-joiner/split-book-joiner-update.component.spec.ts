/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookJoinerUpdate from './split-book-joiner-update.vue';
import SplitBookJoinerService from './split-book-joiner.service';
import AlertService from '@/shared/alert/alert.service';

import SplitBookService from '@/entities/split-book/split-book.service';

type SplitBookJoinerUpdateComponentType = InstanceType<typeof SplitBookJoinerUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookJoinerSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SplitBookJoinerUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SplitBookJoiner Management Update Component', () => {
    let comp: SplitBookJoinerUpdateComponentType;
    let splitBookJoinerServiceStub: SinonStubbedInstance<SplitBookJoinerService>;

    beforeEach(() => {
      route = {};
      splitBookJoinerServiceStub = sinon.createStubInstance<SplitBookJoinerService>(SplitBookJoinerService);
      splitBookJoinerServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          splitBookJoinerService: () => splitBookJoinerServiceStub,
          splitBookService: () =>
            sinon.createStubInstance<SplitBookService>(SplitBookService, {
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
        const wrapper = shallowMount(SplitBookJoinerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBookJoiner = splitBookJoinerSample;
        splitBookJoinerServiceStub.update.resolves(splitBookJoinerSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.update.calledWith(splitBookJoinerSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        splitBookJoinerServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SplitBookJoinerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBookJoiner = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        splitBookJoinerServiceStub.find.resolves(splitBookJoinerSample);
        splitBookJoinerServiceStub.retrieve.resolves([splitBookJoinerSample]);

        // WHEN
        route = {
          params: {
            splitBookJoinerId: '' + splitBookJoinerSample.id,
          },
        };
        const wrapper = shallowMount(SplitBookJoinerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.splitBookJoiner).toMatchObject(splitBookJoinerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookJoinerServiceStub.find.resolves(splitBookJoinerSample);
        const wrapper = shallowMount(SplitBookJoinerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
