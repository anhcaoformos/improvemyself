import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SplitBookDetailService from './split-book-detail.service';
import { type ISplitBookDetail } from '@/shared/model/split-book-detail.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookDetailDetails',
  setup() {
    const splitBookDetailService = inject('splitBookDetailService', () => new SplitBookDetailService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const splitBookDetail: Ref<ISplitBookDetail> = ref({});

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

    return {
      alertService,
      splitBookDetail,

      previousState,
      t$: useI18n().t,
    };
  },
});
