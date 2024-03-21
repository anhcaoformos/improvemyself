/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SplitBookDetailUpdate from './split-book-detail-update.vue';
import SplitBookDetailService from './split-book-detail.service';
import AlertService from '@/shared/alert/alert.service';

import SplitBookJoinerService from '@/entities/split-book-joiner/split-book-joiner.service';
import SplitBookService from '@/entities/split-book/split-book.service';

type SplitBookDetailUpdateComponentType = InstanceType<typeof SplitBookDetailUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const splitBookDetailSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SplitBookDetailUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SplitBookDetail Management Update Component', () => {
    let comp: SplitBookDetailUpdateComponentType;
    let splitBookDetailServiceStub: SinonStubbedInstance<SplitBookDetailService>;

    beforeEach(() => {
      route = {};
      splitBookDetailServiceStub = sinon.createStubInstance<SplitBookDetailService>(SplitBookDetailService);
      splitBookDetailServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          splitBookDetailService: () => splitBookDetailServiceStub,
          splitBookJoinerService: () =>
            sinon.createStubInstance<SplitBookJoinerService>(SplitBookJoinerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
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
        const wrapper = shallowMount(SplitBookDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBookDetail = splitBookDetailSample;
        splitBookDetailServiceStub.update.resolves(splitBookDetailSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.update.calledWith(splitBookDetailSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        splitBookDetailServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SplitBookDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.splitBookDetail = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        splitBookDetailServiceStub.find.resolves(splitBookDetailSample);
        splitBookDetailServiceStub.retrieve.resolves([splitBookDetailSample]);

        // WHEN
        route = {
          params: {
            splitBookDetailId: '' + splitBookDetailSample.id,
          },
        };
        const wrapper = shallowMount(SplitBookDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.splitBookDetail).toMatchObject(splitBookDetailSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        splitBookDetailServiceStub.find.resolves(splitBookDetailSample);
        const wrapper = shallowMount(SplitBookDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
