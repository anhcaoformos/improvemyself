import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TransactionService from './transaction.service';
import { type ITransaction } from '@/shared/model/transaction.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TransactionDetails',
  setup() {
    const transactionService = inject('transactionService', () => new TransactionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const transaction: Ref<ITransaction> = ref({});

    const retrieveTransaction = async transactionId => {
      try {
        const res = await transactionService().find(transactionId);
        transaction.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.transactionId) {
      retrieveTransaction(route.params.transactionId);
    }

    return {
      alertService,
      transaction,

      previousState,
      t$: useI18n().t,
    };
  },
});
