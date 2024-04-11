import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserPaymentMethodService from './user-payment-method.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LedgerService from '@/entities/ledger/ledger.service';
import { type ILedger } from '@/shared/model/ledger.model';
import { type IPaymentMethod, PaymentMethod } from '@/shared/model/payment-method.model';
import { PaymentMethodType } from '@/shared/model/enumerations/payment-method-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentMethodUpdate',
  setup() {
    const paymentMethodService = inject('paymentMethodService', () => new UserPaymentMethodService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentMethod: Ref<IPaymentMethod> = ref(new PaymentMethod());

    const ledgerService = inject('ledgerService', () => new LedgerService());

    const ledgers: Ref<ILedger[]> = ref([]);
    const paymentMethodTypeValues: Ref<string[]> = ref(Object.keys(PaymentMethodType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaymentMethod = async paymentMethodId => {
      try {
        const res = await paymentMethodService().find(paymentMethodId);
        paymentMethod.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentMethodId) {
      retrievePaymentMethod(route.params.paymentMethodId);
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
      code: {},
      name: {},
      type: {},
      isHidden: {},
      ledger: {},
    };
    const v$ = useVuelidate(validationRules, paymentMethod as any);
    v$.value.$validate();

    return {
      paymentMethodService,
      alertService,
      paymentMethod,
      previousState,
      paymentMethodTypeValues,
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
      if (this.paymentMethod.id) {
        this.paymentMethodService()
          .update(this.paymentMethod)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('improvemyselfApp.paymentMethod.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentMethodService()
          .create(this.paymentMethod)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('improvemyselfApp.paymentMethod.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
