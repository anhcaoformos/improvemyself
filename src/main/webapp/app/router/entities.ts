import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');
/* tslint:disable */
// prettier-ignore
const Ledger = () => import('@/entities/ledger/ledger.vue');
const LedgerUpdate = () => import('@/entities/ledger/ledger-update.vue');
const LedgerDetails = () => import('@/entities/ledger/ledger-details.vue');

const Transaction = () => import('@/entities/transaction/transaction.vue');
const TransactionUpdate = () => import('@/entities/transaction/transaction-update.vue');
const TransactionDetails = () => import('@/entities/transaction/transaction-details.vue');

const Goal = () => import('@/entities/goal/goal.vue');
const GoalUpdate = () => import('@/entities/goal/goal-update.vue');
const GoalDetails = () => import('@/entities/goal/goal-details.vue');

const PaymentMethod = () => import('@/entities/payment-method/payment-method.vue');
const PaymentMethodUpdate = () => import('@/entities/payment-method/payment-method-update.vue');
const PaymentMethodDetails = () => import('@/entities/payment-method/payment-method-details.vue');

const PaymentCategory = () => import('@/entities/payment-category/payment-category.vue');
const PaymentCategoryUpdate = () => import('@/entities/payment-category/payment-category-update.vue');
const PaymentCategoryDetails = () => import('@/entities/payment-category/payment-category-details.vue');

const Objective = () => import('@/entities/objective/objective.vue');
const ObjectiveUpdate = () => import('@/entities/objective/objective-update.vue');
const ObjectiveDetails = () => import('@/entities/objective/objective-details.vue');

const SplitBook = () => import('@/entities/split-book/split-book.vue');
const SplitBookUpdate = () => import('@/entities/split-book/split-book-update.vue');
const SplitBookDetails = () => import('@/entities/split-book/split-book-details.vue');

const SplitBookDetail = () => import('@/entities/split-book-detail/split-book-detail.vue');
const SplitBookDetailUpdate = () => import('@/entities/split-book-detail/split-book-detail-update.vue');
const SplitBookDetailDetails = () => import('@/entities/split-book-detail/split-book-detail-details.vue');

const SplitBookJoiner = () => import('@/entities/split-book-joiner/split-book-joiner.vue');
const SplitBookJoinerUpdate = () => import('@/entities/split-book-joiner/split-book-joiner-update.vue');
const SplitBookJoinerDetails = () => import('@/entities/split-book-joiner/split-book-joiner-details.vue');

const Note = () => import('@/entities/note/note.vue');
const NoteUpdate = () => import('@/entities/note/note-update.vue');
const NoteDetails = () => import('@/entities/note/note-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'ledger',
      name: 'Ledger',
      component: Ledger,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'ledger/new',
      name: 'LedgerCreate',
      component: LedgerUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'ledger/:ledgerId/edit',
      name: 'LedgerEdit',
      component: LedgerUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'ledger/:ledgerId/view',
      name: 'LedgerView',
      component: LedgerDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'transaction',
      name: 'Transaction',
      component: Transaction,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'transaction/new',
      name: 'TransactionCreate',
      component: TransactionUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'transaction/:transactionId/edit',
      name: 'TransactionEdit',
      component: TransactionUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'transaction/:transactionId/view',
      name: 'TransactionView',
      component: TransactionDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'goal',
      name: 'Goal',
      component: Goal,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'goal/new',
      name: 'GoalCreate',
      component: GoalUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'goal/:goalId/edit',
      name: 'GoalEdit',
      component: GoalUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'goal/:goalId/view',
      name: 'GoalView',
      component: GoalDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-method',
      name: 'PaymentMethod',
      component: PaymentMethod,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-method/new',
      name: 'PaymentMethodCreate',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-method/:paymentMethodId/edit',
      name: 'PaymentMethodEdit',
      component: PaymentMethodUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-method/:paymentMethodId/view',
      name: 'PaymentMethodView',
      component: PaymentMethodDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-category',
      name: 'PaymentCategory',
      component: PaymentCategory,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-category/new',
      name: 'PaymentCategoryCreate',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-category/:paymentCategoryId/edit',
      name: 'PaymentCategoryEdit',
      component: PaymentCategoryUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'payment-category/:paymentCategoryId/view',
      name: 'PaymentCategoryView',
      component: PaymentCategoryDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'objective',
      name: 'Objective',
      component: Objective,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'objective/new',
      name: 'ObjectiveCreate',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'objective/:objectiveId/edit',
      name: 'ObjectiveEdit',
      component: ObjectiveUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'objective/:objectiveId/view',
      name: 'ObjectiveView',
      component: ObjectiveDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book',
      name: 'SplitBook',
      component: SplitBook,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book/new',
      name: 'SplitBookCreate',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book/:splitBookId/edit',
      name: 'SplitBookEdit',
      component: SplitBookUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book/:splitBookId/view',
      name: 'SplitBookView',
      component: SplitBookDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-detail',
      name: 'SplitBookDetail',
      component: SplitBookDetail,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-detail/new',
      name: 'SplitBookDetailCreate',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/edit',
      name: 'SplitBookDetailEdit',
      component: SplitBookDetailUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-detail/:splitBookDetailId/view',
      name: 'SplitBookDetailView',
      component: SplitBookDetailDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-joiner',
      name: 'SplitBookJoiner',
      component: SplitBookJoiner,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-joiner/new',
      name: 'SplitBookJoinerCreate',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/edit',
      name: 'SplitBookJoinerEdit',
      component: SplitBookJoinerUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'split-book-joiner/:splitBookJoinerId/view',
      name: 'SplitBookJoinerView',
      component: SplitBookJoinerDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'note',
      name: 'Note',
      component: Note,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'note/new',
      name: 'NoteCreate',
      component: NoteUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'note/:noteId/edit',
      name: 'NoteEdit',
      component: NoteUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'note/:noteId/view',
      name: 'NoteView',
      component: NoteDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
