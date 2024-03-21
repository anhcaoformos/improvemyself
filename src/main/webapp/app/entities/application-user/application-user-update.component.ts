import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ApplicationUserService from './application-user.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IApplicationUser, ApplicationUser } from '@/shared/model/application-user.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationUserUpdate',
  setup() {
    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationUser: Ref<IApplicationUser> = ref(new ApplicationUser());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {};
    const v$ = useVuelidate(validationRules, applicationUser as any);
    v$.value.$validate();

    return {
      applicationUserService,
      alertService,
      applicationUser,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.applicationUser.id) {
        this.applicationUserService()
          .update(this.applicationUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.applicationUser.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.applicationUserService()
          .create(this.applicationUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.applicationUser.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
