/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import NoteDetails from './note-details.vue';
import NoteService from './note.service';
import AlertService from '@/shared/alert/alert.service';

type NoteDetailsComponentType = InstanceType<typeof NoteDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const noteSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Note Management Detail Component', () => {
    let noteServiceStub: SinonStubbedInstance<NoteService>;
    let mountOptions: MountingOptions<NoteDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      noteServiceStub = sinon.createStubInstance<NoteService>(NoteService);

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
          noteService: () => noteServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        noteServiceStub.find.resolves(noteSample);
        route = {
          params: {
            noteId: '' + 123,
          },
        };
        const wrapper = shallowMount(NoteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.note).toMatchObject(noteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        noteServiceStub.find.resolves(noteSample);
        const wrapper = shallowMount(NoteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
