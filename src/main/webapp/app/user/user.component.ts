import { defineComponent, provide } from 'vue';

import UserService from './user/user.service';
import UserLedgerService from './ledger/user-ledger.service';
import UserTransactionService from './transaction/user-transaction.service';
import UserGoalService from './goal/user-goal.service';
import UserPaymentMethodService from './payment-method/user-payment-method.service';
import UserPaymentCategoryService from './payment-category/user-payment-category.service';
import UserObjectiveService from './objective/user-objective.service';
import UserSplitBookService from './split-book/user-split-book.service';
import UserSplitBookDetailService from './split-book-detail/user-split-book-detail.service';
import UserSplitBookJoinerService from './split-book-joiner/user-split-book-joiner.service';
import UserNoteService from './note/user-note.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'User',
  setup() {
    provide('userService', () => new UserService());
    provide('ledgerService', () => new UserLedgerService());
    provide('transactionService', () => new UserTransactionService());
    provide('goalService', () => new UserGoalService());
    provide('paymentMethodService', () => new UserPaymentMethodService());
    provide('paymentCategoryService', () => new UserPaymentCategoryService());
    provide('objectiveService', () => new UserObjectiveService());
    provide('splitBookService', () => new UserSplitBookService());
    provide('splitBookDetailService', () => new UserSplitBookDetailService());
    provide('splitBookJoinerService', () => new UserSplitBookJoinerService());
    provide('noteService', () => new UserNoteService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
