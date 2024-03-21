import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import NoteService from './note.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';
import { type IApplicationUser } from '@/shared/model/application-user.model';
import { type INote, Note } from '@/shared/model/note.model';
import { NoteType } from '@/shared/model/enumerations/note-type.model';
import { RepeatType } from '@/shared/model/enumerations/repeat-type.model';
import { AlertType } from '@/shared/model/enumerations/alert-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'NoteUpdate',
  setup() {
    const noteService = inject('noteService', () => new NoteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const note: Ref<INote> = ref(new Note());

    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());

    const applicationUsers: Ref<IApplicationUser[]> = ref([]);
    const noteTypeValues: Ref<string[]> = ref(Object.keys(NoteType));
    const repeatTypeValues: Ref<string[]> = ref(Object.keys(RepeatType));
    const alertTypeValues: Ref<string[]> = ref(Object.keys(AlertType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      applicationUserService()
        .retrieve()
        .then(res => {
          applicationUsers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      title: {},
      description: {},
      noteDateFrom: {},
      noteDateTo: {},
      noteType: {},
      repeatType: {},
      alertType: {},
      applicationUser: {},
    };
    const v$ = useVuelidate(validationRules, note as any);
    v$.value.$validate();

    return {
      noteService,
      alertService,
      note,
      previousState,
      noteTypeValues,
      repeatTypeValues,
      alertTypeValues,
      isSaving,
      currentLanguage,
      applicationUsers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.note.id) {
        this.noteService()
          .update(this.note)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.note.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.noteService()
          .create(this.note)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.note.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
