import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ApplicationUserService from './application-user.service';
import { type IApplicationUser } from '@/shared/model/application-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationUserDetails',
  setup() {
    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const applicationUser: Ref<IApplicationUser> = ref({});

    const retrieveApplicationUser = async applicationUserId => {
      try {
        const res = await applicationUserService().find(applicationUserId);
        applicationUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationUserId) {
      retrieveApplicationUser(route.params.applicationUserId);
    }

    return {
      alertService,
      applicationUser,

      previousState,
      t$: useI18n().t,
    };
  },
});
