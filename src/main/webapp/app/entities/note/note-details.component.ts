import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import NoteService from './note.service';
import { type INote } from '@/shared/model/note.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'NoteDetails',
  setup() {
    const noteService = inject('noteService', () => new NoteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const note: Ref<INote> = ref({});

    const retrieveNote = async noteId => {
      try {
        const res = await noteService().find(noteId);
        note.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.noteId) {
      retrieveNote(route.params.noteId);
    }

    return {
      alertService,
      note,

      previousState,
      t$: useI18n().t,
    };
  },
});
