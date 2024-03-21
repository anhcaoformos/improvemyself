/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import NoteUpdate from './note-update.vue';
import NoteService from './note.service';
import AlertService from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';

type NoteUpdateComponentType = InstanceType<typeof NoteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const noteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<NoteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Note Management Update Component', () => {
    let comp: NoteUpdateComponentType;
    let noteServiceStub: SinonStubbedInstance<NoteService>;

    beforeEach(() => {
      route = {};
      noteServiceStub = sinon.createStubInstance<NoteService>(NoteService);
      noteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          noteService: () => noteServiceStub,
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
        const wrapper = shallowMount(NoteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.note = noteSample;
        noteServiceStub.update.resolves(noteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(noteServiceStub.update.calledWith(noteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        noteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(NoteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.note = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(noteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        noteServiceStub.find.resolves(noteSample);
        noteServiceStub.retrieve.resolves([noteSample]);

        // WHEN
        route = {
          params: {
            noteId: '' + noteSample.id,
          },
        };
        const wrapper = shallowMount(NoteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.note).toMatchObject(noteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        noteServiceStub.find.resolves(noteSample);
        const wrapper = shallowMount(NoteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
