import { type ComputedRef, defineComponent, inject, ref, type Ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';

import type LoginService from '@/account/login.service';
import type AccountService from '@/account/account.service';
import type { ILedger } from '../../shared/model/ledger.model';
import HomeService from './home.service';
import UserLedgerService from '../../user/ledger/user-ledger.service';
import { useAlertService } from '../../shared/alert/alert.service';
import type { IHome } from '../../shared/model/home.model';
import VueMultiselect from 'vue-multiselect';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const loginService = inject<LoginService>('loginService');
    const accountService = inject<AccountService>('accountService');
    const ledgerService = inject('ledgerService', () => new UserLedgerService());
    const homeService = inject('homeService', () => new HomeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const hasAnyAuthorityValues: Ref<any> = ref({});
    const hasNotAuthorityValues: Ref<string[]> = ref([]);
    const userLedgers: Ref<ILedger[]> = ref([]);
    const selectedUserLedger: Ref<ILedger> = ref({});
    const isFetching = ref(false);

    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const getDashboard = async () => {
      isFetching.value = true;
      try {
        const res = await homeService().getDashboard();
        userLedgers.value = res.ledgers ?? [];
        selectedUserLedger.value = res.ledger ?? {};
        console.log(userLedgers);
        console.log(selectedUserLedger);
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };
    const openLogin = () => {
      loginService.openLogin();
    };

    const handleSyncList = () => {
      getDashboard();
    };

    onMounted(async () => {
      await getDashboard();
    });

    return {
      accountService,
      homeService,
      hasNotAuthorityValues,
      hasAnyAuthorityValues,
      authenticated,
      username,
      openLogin,
      handleSyncList,
      selectedUserLedger,
      userLedgers,
      t$: useI18n().t,
    };
  },
  data() {
    return {
      userLedgers: [{ name: 'ac' }, { name: 'ab' }],
      selectedUserLedger: { name: 'ac' },
    };
  },
  methods: {
    hasAnyAuthority(authorities: any): boolean {
      this.accountService.hasAnyAuthorityAndCheckAuth(authorities).then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
      return this.hasAnyAuthorityValues[authorities] ?? false;
    },
    hasNotAuthorities(authorities: string[]): boolean {
      this.accountService.hasAnyAuthorityAndCheckAuth(authorities).then(value => {
        if (this.hasNotAuthorityValues[authorities] !== value) {
          this.hasNotAuthorityValues = { ...this.hasNotAuthorityValues, [authorities]: value };
        }
      });
      return !(this.hasNotAuthorityValues[authorities] ?? false);
    },
  },
  components: { VueMultiselect },
});
