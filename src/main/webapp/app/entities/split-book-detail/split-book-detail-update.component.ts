import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SplitBookDetailService from './split-book-detail.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SplitBookJoinerService from '@/entities/split-book-joiner/split-book-joiner.service';
import { type ISplitBookJoiner } from '@/shared/model/split-book-joiner.model';
import SplitBookService from '@/entities/split-book/split-book.service';
import { type ISplitBook } from '@/shared/model/split-book.model';
import { type ISplitBookDetail, SplitBookDetail } from '@/shared/model/split-book-detail.model';
import { TransactionType } from '@/shared/model/enumerations/transaction-type.model';
import { JoinerRole } from '@/shared/model/enumerations/joiner-role.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookDetailUpdate',
  setup() {
    const splitBookDetailService = inject('splitBookDetailService', () => new SplitBookDetailService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const splitBookDetail: Ref<ISplitBookDetail> = ref(new SplitBookDetail());

    const splitBookJoinerService = inject('splitBookJoinerService', () => new SplitBookJoinerService());

    const splitBookJoiners: Ref<ISplitBookJoiner[]> = ref([]);

    const splitBookService = inject('splitBookService', () => new SplitBookService());

    const splitBooks: Ref<ISplitBook[]> = ref([]);
    const transactionTypeValues: Ref<string[]> = ref(Object.keys(TransactionType));
    const joinerRoleValues: Ref<string[]> = ref(Object.keys(JoinerRole));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSplitBookDetail = async splitBookDetailId => {
      try {
        const res = await splitBookDetailService().find(splitBookDetailId);
        splitBookDetail.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.splitBookDetailId) {
      retrieveSplitBookDetail(route.params.splitBookDetailId);
    }

    const initRelationships = () => {
      splitBookJoinerService()
        .retrieve()
        .then(res => {
          splitBookJoiners.value = res.data;
        });
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
      amount: {},
      description: {},
      personName: {},
      transactionDate: {},
      transactionType: {},
      groupId: {},
      joinerRole: {},
      splitBookJoiner: {},
      splitBook: {},
    };
    const v$ = useVuelidate(validationRules, splitBookDetail as any);
    v$.value.$validate();

    return {
      splitBookDetailService,
      alertService,
      splitBookDetail,
      previousState,
      transactionTypeValues,
      joinerRoleValues,
      isSaving,
      currentLanguage,
      splitBookJoiners,
      splitBooks,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.splitBookDetail.id) {
        this.splitBookDetailService()
          .update(this.splitBookDetail)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.splitBookDetail.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.splitBookDetailService()
          .create(this.splitBookDetail)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.splitBookDetail.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
