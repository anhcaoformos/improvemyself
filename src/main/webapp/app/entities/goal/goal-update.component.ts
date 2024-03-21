import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import GoalService from './goal.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';
import { type ILedger } from '@/shared/model/ledger.model';
import { type IGoal, Goal } from '@/shared/model/goal.model';
import { Priority } from '@/shared/model/enumerations/priority.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'GoalUpdate',
  setup() {
    const goalService = inject('goalService', () => new GoalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const goal: Ref<IGoal> = ref(new Goal());

    const ledgerService = inject('ledgerService', () => new LedgerService());

    const ledgers: Ref<ILedger[]> = ref([]);
    const priorityValues: Ref<string[]> = ref(Object.keys(Priority));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveGoal = async goalId => {
      try {
        const res = await goalService().find(goalId);
        goal.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.goalId) {
      retrieveGoal(route.params.goalId);
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
      title: {},
      description: {},
      priority: {},
      ledger: {},
    };
    const v$ = useVuelidate(validationRules, goal as any);
    v$.value.$validate();

    return {
      goalService,
      alertService,
      goal,
      previousState,
      priorityValues,
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
      if (this.goal.id) {
        this.goalService()
          .update(this.goal)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.goal.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.goalService()
          .create(this.goal)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.goal.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
