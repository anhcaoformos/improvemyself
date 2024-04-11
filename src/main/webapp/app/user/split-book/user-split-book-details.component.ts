import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserSplitBookService from './user-split-book.service';
import { type ISplitBook } from '@/shared/model/split-book.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SplitBookDetails',
  setup() {
    const splitBookService = inject('splitBookService', () => new UserSplitBookService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const splitBook: Ref<ISplitBook> = ref({});

    const retrieveSplitBook = async splitBookId => {
      try {
        const res = await splitBookService().find(splitBookId);
        splitBook.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.splitBookId) {
      retrieveSplitBook(route.params.splitBookId);
    }

    return {
      alertService,
      splitBook,

      previousState,
      t$: useI18n().t,
    };
  },
});
