import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PaymentCategoryService from './payment-category.service';
import { type IPaymentCategory } from '@/shared/model/payment-category.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentCategoryDetails',
  setup() {
    const paymentCategoryService = inject('paymentCategoryService', () => new PaymentCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paymentCategory: Ref<IPaymentCategory> = ref({});

    const retrievePaymentCategory = async paymentCategoryId => {
      try {
        const res = await paymentCategoryService().find(paymentCategoryId);
        paymentCategory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentCategoryId) {
      retrievePaymentCategory(route.params.paymentCategoryId);
    }

    return {
      alertService,
      paymentCategory,

      previousState,
      t$: useI18n().t,
    };
  },
});
