import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserSplitBookJoinerService from './user-split-book-joiner.service';
import { type ISplitBookJoiner } from '@/shared/model/split-book-joiner.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookJoinerDetails',
  setup() {
    const splitBookJoinerService = inject('splitBookJoinerService', () => new UserSplitBookJoinerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const splitBookJoiner: Ref<ISplitBookJoiner> = ref({});

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

    return {
      alertService,
      splitBookJoiner,

      previousState,
      t$: useI18n().t,
    };
  },
});
