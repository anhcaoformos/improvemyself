import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserObjectiveService from './user-objective.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PaymentCategoryService from '@/entities/payment-category/payment-category.service';
import { type IPaymentCategory } from '@/shared/model/payment-category.model';
import LedgerService from '@/entities/ledger/ledger.service';
import { type ILedger } from '@/shared/model/ledger.model';
import { type IObjective, Objective } from '@/shared/model/objective.model';
import { ObjectiveType } from '@/shared/model/enumerations/objective-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ObjectiveUpdate',
  setup() {
    const objectiveService = inject('objectiveService', () => new UserObjectiveService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const objective: Ref<IObjective> = ref(new Objective());

    const paymentCategoryService = inject('paymentCategoryService', () => new PaymentCategoryService());

    const paymentCategories: Ref<IPaymentCategory[]> = ref([]);

    const ledgerService = inject('ledgerService', () => new LedgerService());

    const ledgers: Ref<ILedger[]> = ref([]);
    const objectiveTypeValues: Ref<string[]> = ref(Object.keys(ObjectiveType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveObjective = async objectiveId => {
      try {
        const res = await objectiveService().find(objectiveId);
        objective.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.objectiveId) {
      retrieveObjective(route.params.objectiveId);
    }

    const initRelationships = () => {
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
      name: {},
      type: {},
      isHidden: {},
      paymentCategory: {},
      ledger: {},
    };
    const v$ = useVuelidate(validationRules, objective as any);
    v$.value.$validate();

    return {
      objectiveService,
      alertService,
      objective,
      previousState,
      objectiveTypeValues,
      isSaving,
      currentLanguage,
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
      if (this.objective.id) {
        this.objectiveService()
          .update(this.objective)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.objective.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.objectiveService()
          .create(this.objective)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.objective.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
