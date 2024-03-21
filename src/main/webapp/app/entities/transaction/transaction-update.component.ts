import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TransactionService from './transaction.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ObjectiveService from '@/entities/objective/objective.service';
import { type IObjective } from '@/shared/model/objective.model';
import PaymentMethodService from '@/entities/payment-method/payment-method.service';
import { type IPaymentMethod } from '@/shared/model/payment-method.model';
import PaymentCategoryService from '@/entities/payment-category/payment-category.service';
import { type IPaymentCategory } from '@/shared/model/payment-category.model';
import LedgerService from '@/entities/ledger/ledger.service';
import { type ILedger } from '@/shared/model/ledger.model';
import { type ITransaction, Transaction } from '@/shared/model/transaction.model';
import { TransactionType } from '@/shared/model/enumerations/transaction-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TransactionUpdate',
  setup() {
    const transactionService = inject('transactionService', () => new TransactionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const transaction: Ref<ITransaction> = ref(new Transaction());

    const objectiveService = inject('objectiveService', () => new ObjectiveService());

    const objectives: Ref<IObjective[]> = ref([]);

    const paymentMethodService = inject('paymentMethodService', () => new PaymentMethodService());

    const paymentMethods: Ref<IPaymentMethod[]> = ref([]);

    const paymentCategoryService = inject('paymentCategoryService', () => new PaymentCategoryService());

    const paymentCategories: Ref<IPaymentCategory[]> = ref([]);

    const ledgerService = inject('ledgerService', () => new LedgerService());

    const ledgers: Ref<ILedger[]> = ref([]);
    const transactionTypeValues: Ref<string[]> = ref(Object.keys(TransactionType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTransaction = async transactionId => {
      try {
        const res = await transactionService().find(transactionId);
        transaction.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.transactionId) {
      retrieveTransaction(route.params.transactionId);
    }

    const initRelationships = () => {
      objectiveService()
        .retrieve()
        .then(res => {
          objectives.value = res.data;
        });
      paymentMethodService()
        .retrieve()
        .then(res => {
          paymentMethods.value = res.data;
        });
      paymentCategoryService()
        .retrieve()
        .then(res => {
          paymentCategories.value = res.data;
        });
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
      amount: {},
      description: {},
      transactionDate: {},
      transactionType: {},
      objective: {},
      paymentMethod: {},
      paymentCategory: {},
      ledger: {},
    };
    const v$ = useVuelidate(validationRules, transaction as any);
    v$.value.$validate();

    return {
      transactionService,
      alertService,
      transaction,
      previousState,
      transactionTypeValues,
      isSaving,
      currentLanguage,
      objectives,
      paymentMethods,
      paymentCategories,
      ledgers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.transaction.id) {
        this.transactionService()
          .update(this.transaction)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.transaction.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.transactionService()
          .create(this.transaction)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.transaction.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
