import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import LedgerService from './ledger.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';
import { type IApplicationUser } from '@/shared/model/application-user.model';
import { type ILedger, Ledger } from '@/shared/model/ledger.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LedgerUpdate',
  setup() {
    const ledgerService = inject('ledgerService', () => new LedgerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const ledger: Ref<ILedger> = ref(new Ledger());

    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());

    const applicationUsers: Ref<IApplicationUser[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveLedger = async ledgerId => {
      try {
        const res = await ledgerService().find(ledgerId);
        ledger.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.ledgerId) {
      retrieveLedger(route.params.ledgerId);
    }

    const initRelationships = () => {
      applicationUserService()
        .retrieve()
        .then(res => {
          applicationUsers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      isDefault: {},
      applicationUser: {},
    };
    const v$ = useVuelidate(validationRules, ledger as any);
    v$.value.$validate();

    return {
      ledgerService,
      alertService,
      ledger,
      previousState,
      isSaving,
      currentLanguage,
      applicationUsers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.ledger.id) {
        this.ledgerService()
          .update(this.ledger)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.ledger.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.ledgerService()
          .create(this.ledger)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.ledger.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
