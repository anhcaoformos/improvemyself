import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserGoalService from './user-goal.service';
import { type IGoal } from '@/shared/model/goal.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'GoalDetails',
  setup() {
    const goalService = inject('goalService', () => new UserGoalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const goal: Ref<IGoal> = ref({});

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

    return {
      alertService,
      goal,

      previousState,
      t$: useI18n().t,
    };
  },
});
