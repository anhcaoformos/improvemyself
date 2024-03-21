import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentCategoryService from './payment-category.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';
import { type ILedger } from '@/shared/model/ledger.model';
import { type IPaymentCategory, PaymentCategory } from '@/shared/model/payment-category.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentCategoryUpdate',
  setup() {
    const paymentCategoryService = inject('paymentCategoryService', () => new PaymentCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentCategory: Ref<IPaymentCategory> = ref(new PaymentCategory());

    const ledgerService = inject('ledgerService', () => new LedgerService());

    const ledgers: Ref<ILedger[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaymentCategory = async paymentCategoryId => {
      try {
        const res = await paymentCategoryService().find(paymentCategoryId);
        paymentCategory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentCategoryId) {
      retrievePaymentCategory(route.params.paymentCategoryId);
    }

    const initRelationships = () => {
      ledgerService()
        .retrieve()
        .then(res => {
          ledgers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      code: {},
      name: {},
      isDefault: {},
      isHidden: {},
      ledger: {},
    };
    const v$ = useVuelidate(validationRules, paymentCategory as any);
    v$.value.$validate();

    return {
      paymentCategoryService,
      alertService,
      paymentCategory,
      previousState,
      isSaving,
      currentLanguage,
      ledgers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paymentCategory.id) {
        this.paymentCategoryService()
          .update(this.paymentCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.paymentCategory.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentCategoryService()
          .create(this.paymentCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.paymentCategory.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
