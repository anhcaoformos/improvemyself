<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentCategoryHeading">
      <span v-text="t$('improvemyselfApp.paymentCategory.home.title')" id="payment-category-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.paymentCategory.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentCategoryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-category"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.paymentCategory.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentCategories && paymentCategories.length === 0">
      <span v-text="t$('improvemyselfApp.paymentCategory.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="paymentCategories && paymentCategories.length > 0">
      <table class="table table-striped" aria-describedby="paymentCategories">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('code')">
              <span v-text="t$('improvemyselfApp.paymentCategory.code')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="t$('improvemyselfApp.paymentCategory.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isDefault')">
              <span v-text="t$('improvemyselfApp.paymentCategory.isDefault')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isDefault'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isHidden')">
              <span v-text="t$('improvemyselfApp.paymentCategory.isHidden')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isHidden'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ledger.id')">
              <span v-text="t$('improvemyselfApp.paymentCategory.ledger')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ledger.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentCategory in paymentCategories" :key="paymentCategory.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: paymentCategory.id } }">{{
                paymentCategory.id
              }}</router-link>
            </td>
            <td>{{ paymentCategory.code }}</td>
            <td>{{ paymentCategory.name }}</td>
            <td>{{ paymentCategory.isDefault }}</td>
            <td>{{ paymentCategory.isHidden }}</td>
            <td>
              <div v-if="paymentCategory.ledger">
                <router-link :to="{ name: 'LedgerView', params: { ledgerId: paymentCategory.ledger.id } }">{{
                  paymentCategory.ledger.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: paymentCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PaymentCategoryEdit', params: { paymentCategoryId: paymentCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(paymentCategory)"
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
          id="improvemyselfApp.paymentCategory.delete.question"
          data-cy="paymentCategoryDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paymentCategory-heading" v-text="t$('improvemyselfApp.paymentCategory.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paymentCategory"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removePaymentCategory()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="paymentCategories && paymentCategories.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-category.component.ts"></script>
