<template>
  <div>
    <h2 id="page-heading" data-cy="TransactionHeading">
      <span v-text="t$('improvemyselfApp.transaction.home.title')" id="transaction-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.transaction.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TransactionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-transaction"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.transaction.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && transactions && transactions.length === 0">
      <span v-text="t$('improvemyselfApp.transaction.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="transactions && transactions.length > 0">
      <table class="table table-striped" aria-describedby="transactions">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="t$('improvemyselfApp.transaction.amount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="t$('improvemyselfApp.transaction.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('transactionDate')">
              <span v-text="t$('improvemyselfApp.transaction.transactionDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'transactionDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('transactionType')">
              <span v-text="t$('improvemyselfApp.transaction.transactionType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'transactionType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('objective.id')">
              <span v-text="t$('improvemyselfApp.transaction.objective')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'objective.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentMethod.id')">
              <span v-text="t$('improvemyselfApp.transaction.paymentMethod')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentMethod.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentCategory.id')">
              <span v-text="t$('improvemyselfApp.transaction.paymentCategory')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentCategory.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ledger.id')">
              <span v-text="t$('improvemyselfApp.transaction.ledger')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ledger.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="transaction in transactions" :key="transaction.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TransactionView', params: { transactionId: transaction.id } }">{{ transaction.id }}</router-link>
            </td>
            <td>{{ transaction.amount }}</td>
            <td>{{ transaction.description }}</td>
            <td>{{ transaction.transactionDate }}</td>
            <td v-text="t$('improvemyselfApp.TransactionType.' + transaction.transactionType)"></td>
            <td>
              <div v-if="transaction.objective">
                <router-link :to="{ name: 'ObjectiveView', params: { objectiveId: transaction.objective.id } }">{{
                  transaction.objective.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="transaction.paymentMethod">
                <router-link :to="{ name: 'PaymentMethodView', params: { paymentMethodId: transaction.paymentMethod.id } }">{{
                  transaction.paymentMethod.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="transaction.paymentCategory">
                <router-link :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: transaction.paymentCategory.id } }">{{
                  transaction.paymentCategory.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="transaction.ledger">
                <router-link :to="{ name: 'LedgerView', params: { ledgerId: transaction.ledger.id } }">{{
                  transaction.ledger.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TransactionView', params: { transactionId: transaction.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TransactionEdit', params: { transactionId: transaction.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(transaction)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="improvemyselfApp.transaction.delete.question"
          data-cy="transactionDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-transaction-heading" v-text="t$('improvemyselfApp.transaction.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-transaction"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeTransaction()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="transactions && transactions.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./transaction.component.ts"></script>
