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
      name: 'Ledger',
      component: Ledger,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/new',
      name: 'LedgerCreate',
      component: LedgerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/:ledgerId/edit',
      name: 'LedgerEdit',
      component: LedgerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ledger/:ledgerId/view',
      name: 'LedgerView',
      component: LedgerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction',
      name: 'Transaction',
      component: Transaction,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/new',
      name: 'TransactionCreate',
      component: TransactionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/:transactionId/edit',
      name: 'TransactionEdit',
      component: TransactionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'transaction/:transactionId/view',
      name: 'TransactionView',
      component: TransactionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal',
      name: 'Goal',
      component: Goal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/new',
      name: 'GoalCreate',
      component: GoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/:goalId/edit',
      name: 'GoalEdit',
      component: GoalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'goal/:goalId/view',
      name: 'GoalView',
      component: GoalDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method',
      name: 'PaymentMethod',
      component: PaymentMethod,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/new',
      name: 'PaymentMethodCreate',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/:paymentMethodId/edit',
      name: 'PaymentMethodEdit',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-method/:paymentMethodId/view',
      name: 'PaymentMethodView',
      component: PaymentMethodDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category',
      name: 'PaymentCategory',
      component: PaymentCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/new',
      name: 'PaymentCategoryCreate',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/:paymentCategoryId/edit',
      name: 'PaymentCategoryEdit',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-category/:paymentCategoryId/view',
      name: 'PaymentCategoryView',
      component: PaymentCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective',
      name: 'Objective',
      component: Objective,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/new',
      name: 'ObjectiveCreate',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/:objectiveId/edit',
      name: 'ObjectiveEdit',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'objective/:objectiveId/view',
      name: 'ObjectiveView',
      component: ObjectiveDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book',
      name: 'SplitBook',
      component: SplitBook,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/new',
      name: 'SplitBookCreate',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/:splitBookId/edit',
      name: 'SplitBookEdit',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book/:splitBookId/view',
      name: 'SplitBookView',
      component: SplitBookDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail',
      name: 'SplitBookDetail',
      component: SplitBookDetail,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/new',
      name: 'SplitBookDetailCreate',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/edit',
      name: 'SplitBookDetailEdit',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/view',
      name: 'SplitBookDetailView',
      component: SplitBookDetailDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner',
      name: 'SplitBookJoiner',
      component: SplitBookJoiner,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/new',
      name: 'SplitBookJoinerCreate',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/edit',
      name: 'SplitBookJoinerEdit',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/view',
      name: 'SplitBookJoinerView',
      component: SplitBookJoinerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note',
      name: 'Note',
      component: Note,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/new',
      name: 'NoteCreate',
      component: NoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/:noteId/edit',
      name: 'NoteEdit',
      component: NoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'note/:noteId/view',
      name: 'NoteView',
      component: NoteDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add user to the router here
  ],
};
