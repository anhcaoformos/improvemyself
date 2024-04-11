import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserPaymentMethodService from './user-payment-method.service';
import { type IPaymentMethod } from '@/shared/model/payment-method.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentMethodDetails',
  setup() {
    const paymentMethodService = inject('paymentMethodService', () => new UserPaymentMethodService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paymentMethod: Ref<IPaymentMethod> = ref({});

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

    return {
      alertService,
      paymentMethod,

      previousState,
      t$: useI18n().t,
    };
  },
});
