/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import GoalUpdate from './goal-update.vue';
import GoalService from './goal.service';
import AlertService from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';

type GoalUpdateComponentType = InstanceType<typeof GoalUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const goalSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<GoalUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Goal Management Update Component', () => {
    let comp: GoalUpdateComponentType;
    let goalServiceStub: SinonStubbedInstance<GoalService>;

    beforeEach(() => {
      route = {};
      goalServiceStub = sinon.createStubInstance<GoalService>(GoalService);
      goalServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          goalService: () => goalServiceStub,
          ledgerService: () =>
            sinon.createStubInstance<LedgerService>(LedgerService, {
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
        const wrapper = shallowMount(GoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.goal = goalSample;
        goalServiceStub.update.resolves(goalSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(goalServiceStub.update.calledWith(goalSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        goalServiceStub.create.resolves(entity);
        const wrapper = shallowMount(GoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.goal = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(goalServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        goalServiceStub.find.resolves(goalSample);
        goalServiceStub.retrieve.resolves([goalSample]);

        // WHEN
        route = {
          params: {
            goalId: '' + goalSample.id,
          },
        };
        const wrapper = shallowMount(GoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.goal).toMatchObject(goalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        goalServiceStub.find.resolves(goalSample);
        const wrapper = shallowMount(GoalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
