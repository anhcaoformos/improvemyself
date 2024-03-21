import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ObjectiveService from './objective.service';
import { type IObjective } from '@/shared/model/objective.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ObjectiveDetails',
  setup() {
    const objectiveService = inject('objectiveService', () => new ObjectiveService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const objective: Ref<IObjective> = ref({});

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

    return {
      alertService,
      objective,

      previousState,
      t$: useI18n().t,
    };
  },
});
