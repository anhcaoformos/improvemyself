import { defineComponent, provide } from 'vue';

import ApplicationUserService from './application-user/application-user.service';
import LedgerService from './ledger/ledger.service';
import TransactionService from './transaction/transaction.service';
import GoalService from './goal/goal.service';
import PaymentMethodService from './payment-method/payment-method.service';
import PaymentCategoryService from './payment-category/payment-category.service';
import ObjectiveService from './objective/objective.service';
import SplitBookService from './split-book/split-book.service';
import SplitBookDetailService from './split-book-detail/split-book-detail.service';
import SplitBookJoinerService from './split-book-joiner/split-book-joiner.service';
import NoteService from './note/note.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('applicationUserService', () => new ApplicationUserService());
    provide('ledgerService', () => new LedgerService());
    provide('transactionService', () => new TransactionService());
    provide('goalService', () => new GoalService());
    provide('paymentMethodService', () => new PaymentMethodService());
    provide('paymentCategoryService', () => new PaymentCategoryService());
    provide('objectiveService', () => new ObjectiveService());
    provide('splitBookService', () => new SplitBookService());
    provide('splitBookDetailService', () => new SplitBookDetailService());
    provide('splitBookJoinerService', () => new SplitBookJoinerService());
    provide('noteService', () => new NoteService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
