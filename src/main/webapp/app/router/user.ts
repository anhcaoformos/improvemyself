import { Authority } from '@/shared/security/authority';
const User = () => import('@/user/user.vue');
/* tslint:disable */
// prettier-ignore
const Ledger = () => import('@/user/ledger/user-ledger.vue');
const LedgerUpdate = () => import('@/user/ledger/user-ledger-update.vue');
const LedgerDetails = () => import('@/user/ledger/user-ledger-details.vue');

const Transaction = () => import('@/user/transaction/user-transaction.vue');
const TransactionUpdate = () => import('@/user/transaction/user-transaction-update.vue');
const TransactionDetails = () => import('@/user/transaction/user-transaction-details.vue');

const Goal = () => import('@/user/goal/user-goal.vue');
const GoalUpdate = () => import('@/user/goal/user-goal-update.vue');
const GoalDetails = () => import('@/user/goal/user-goal-details.vue');

const PaymentMethod = () => import('@/user/payment-method/user-payment-method.vue');
const PaymentMethodUpdate = () => import('@/user/payment-method/user-payment-method-update.vue');
const PaymentMethodDetails = () => import('@/user/payment-method/user-payment-method-details.vue');

const PaymentCategory = () => import('@/user/payment-category/user-payment-category.vue');
const PaymentCategoryUpdate = () => import('@/user/payment-category/user-payment-category-update.vue');
const PaymentCategoryDetails = () => import('@/user/payment-category/user-payment-category-details.vue');

const Objective = () => import('@/user/objective/user-objective.vue');
const ObjectiveUpdate = () => import('@/user/objective/user-objective-update.vue');
const ObjectiveDetails = () => import('@/user/objective/user-objective-details.vue');

const SplitBook = () => import('@/user/split-book/user-split-book.vue');
const SplitBookUpdate = () => import('@/user/split-book/user-split-book-update.vue');
const SplitBookDetails = () => import('@/user/split-book/user-split-book-details.vue');

const SplitBookDetail = () => import('@/user/split-book-detail/user-split-book-detail.vue');
const SplitBookDetailUpdate = () => import('@/user/split-book-detail/user-split-book-detail-update.vue');
const SplitBookDetailDetails = () => import('@/user/split-book-detail/user-split-book-detail-details.vue');

const SplitBookJoiner = () => import('@/user/split-book-joiner/user-split-book-joiner.vue');
const SplitBookJoinerUpdate = () => import('@/user/split-book-joiner/user-split-book-joiner-update.vue');
const SplitBookJoinerDetails = () => import('@/user/split-book-joiner/user-split-book-joiner-details.vue');

const Note = () => import('@/user/note/user-note.vue');
const NoteUpdate = () => import('@/user/note/user-note-update.vue');
const NoteDetails = () => import('@/user/note/user-note-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import user to the router here

export default {
  path: '/user',
  component: User,
  children: [
    {
      path: 'ledger',
      name: 'UserLedger',
      component: Ledger,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/new',
      name: 'UserLedgerCreate',
      component: LedgerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/:ledgerId/edit',
      name: 'UserLedgerEdit',
      component: LedgerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/:ledgerId/view',
      name: 'UserLedgerView',
      component: LedgerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction',
      name: 'UserTransaction',
      component: Transaction,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/new',
      name: 'UserTransactionCreate',
      component: TransactionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/:transactionId/edit',
      name: 'UserTransactionEdit',
      component: TransactionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/:transactionId/view',
      name: 'UserTransactionView',
      component: TransactionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal',
      name: 'UserGoal',
      component: Goal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/new',
      name: 'UserGoalCreate',
      component: GoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/:goalId/edit',
      name: 'UserGoalEdit',
      component: GoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/:goalId/view',
      name: 'UserGoalView',
      component: GoalDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method',
      name: 'UserPaymentMethod',
      component: PaymentMethod,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/new',
      name: 'UserPaymentMethodCreate',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/:paymentMethodId/edit',
      name: 'UserPaymentMethodEdit',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/:paymentMethodId/view',
      name: 'UserPaymentMethodView',
      component: PaymentMethodDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category',
      name: 'UserPaymentCategory',
      component: PaymentCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/new',
      name: 'UserPaymentCategoryCreate',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/:paymentCategoryId/edit',
      name: 'UserPaymentCategoryEdit',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/:paymentCategoryId/view',
      name: 'UserPaymentCategoryView',
      component: PaymentCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective',
      name: 'UserObjective',
      component: Objective,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/new',
      name: 'UserObjectiveCreate',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/:objectiveId/edit',
      name: 'UserObjectiveEdit',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/:objectiveId/view',
      name: 'UserObjectiveView',
      component: ObjectiveDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book',
      name: 'UserSplitBook',
      component: SplitBook,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/new',
      name: 'UserSplitBookCreate',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/:splitBookId/edit',
      name: 'UserSplitBookEdit',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/:splitBookId/view',
      name: 'UserSplitBookView',
      component: SplitBookDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail',
      name: 'UserSplitBookDetail',
      component: SplitBookDetail,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/new',
      name: 'UserSplitBookDetailCreate',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/edit',
      name: 'UserSplitBookDetailEdit',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/view',
      name: 'UserSplitBookDetailView',
      component: SplitBookDetailDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner',
      name: 'UserSplitBookJoiner',
      component: SplitBookJoiner,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/new',
      name: 'UserSplitBookJoinerCreate',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/edit',
      name: 'UserSplitBookJoinerEdit',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/view',
      name: 'UserSplitBookJoinerView',
      component: SplitBookJoinerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note',
      name: 'UserNote',
      component: Note,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/new',
      name: 'UserNoteCreate',
      component: NoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/:noteId/edit',
      name: 'UserNoteEdit',
      component: NoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/:noteId/view',
      name: 'UserNoteView',
      component: NoteDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add user to the router here
  ],
};
