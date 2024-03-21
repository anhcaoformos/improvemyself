import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SplitBookJoinerService from './split-book-joiner.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SplitBookService from '@/entities/split-book/split-book.service';
import { type ISplitBook } from '@/shared/model/split-book.model';
import { type ISplitBookJoiner, SplitBookJoiner } from '@/shared/model/split-book-joiner.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookJoinerUpdate',
  setup() {
    const splitBookJoinerService = inject('splitBookJoinerService', () => new SplitBookJoinerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const splitBookJoiner: Ref<ISplitBookJoiner> = ref(new SplitBookJoiner());

    const splitBookService = inject('splitBookService', () => new SplitBookService());

    const splitBooks: Ref<ISplitBook[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSplitBookJoiner = async splitBookJoinerId => {
      try {
        const res = await splitBookJoinerService().find(splitBookJoinerId);
        splitBookJoiner.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.splitBookJoinerId) {
      retrieveSplitBookJoiner(route.params.splitBookJoinerId);
    }

    const initRelationships = () => {
      splitBookService()
        .retrieve()
        .then(res => {
          splitBooks.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      splitBook: {},
    };
    const v$ = useVuelidate(validationRules, splitBookJoiner as any);
    v$.value.$validate();

    return {
      splitBookJoinerService,
      alertService,
      splitBookJoiner,
      previousState,
      isSaving,
      currentLanguage,
      splitBooks,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.splitBookJoiner.id) {
        this.splitBookJoinerService()
          .update(this.splitBookJoiner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.splitBookJoiner.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.splitBookJoinerService()
          .create(this.splitBookJoiner)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.splitBookJoiner.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
