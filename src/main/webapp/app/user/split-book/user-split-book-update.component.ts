import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserSplitBookService from './user-split-book.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IUser } from '@/shared/model/user.model';
import { type ISplitBook, SplitBook } from '@/shared/model/split-book.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookUpdate',
  setup() {
    const splitBookService = inject('splitBookService', () => new UserSplitBookService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const splitBook: Ref<ISplitBook> = ref(new SplitBook());

    const userService = inject('userService', () => new UserService());

    const users: Ref<IUser[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSplitBook = async splitBookId => {
      try {
        const res = await splitBookService().find(splitBookId);
        splitBook.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.splitBookId) {
      retrieveSplitBook(route.params.splitBookId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      description: {},
      name: {},
      user: {},
    };
    const v$ = useVuelidate(validationRules, splitBook as any);
    v$.value.$validate();

    return {
      splitBookService,
      alertService,
      splitBook,
      previousState,
      isSaving,
      currentLanguage,
      users,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.splitBook.id) {
        this.splitBookService()
          .update(this.splitBook)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.splitBook.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.splitBookService()
          .create(this.splitBook)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.splitBook.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
