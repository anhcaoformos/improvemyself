<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentMethodHeading">
      <span v-text="t$('improvemyselfApp.paymentMethod.home.title')" id="payment-method-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.paymentMethod.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentMethodCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-method"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.paymentMethod.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentMethods && paymentMethods.length === 0">
      <span v-text="t$('improvemyselfApp.paymentMethod.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="paymentMethods && paymentMethods.length > 0">
      <table class="table table-striped" aria-describedby="paymentMethods">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('code')">
              <span v-text="t$('improvemyselfApp.paymentMethod.code')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="t$('improvemyselfApp.paymentMethod.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('type')">
              <span v-text="t$('improvemyselfApp.paymentMethod.type')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isHidden')">
              <span v-text="t$('improvemyselfApp.paymentMethod.isHidden')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isHidden'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ledger.id')">
              <span v-text="t$('improvemyselfApp.paymentMethod.ledger')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ledger.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentMethod in paymentMethods" :key="paymentMethod.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentMethodView', params: { paymentMethodId: paymentMethod.id } }">{{
                paymentMethod.id
              }}</router-link>
            </td>
            <td>{{ paymentMethod.code }}</td>
            <td>{{ paymentMethod.name }}</td>
            <td v-text="t$('improvemyselfApp.PaymentMethodType.' + paymentMethod.type)"></td>
            <td>{{ paymentMethod.isHidden }}</td>
            <td>
              <div v-if="paymentMethod.ledger">
                <router-link :to="{ name: 'LedgerView', params: { ledgerId: paymentMethod.ledger.id } }">{{
                  paymentMethod.ledger.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PaymentMethodView', params: { paymentMethodId: paymentMethod.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PaymentMethodEdit', params: { paymentMethodId: paymentMethod.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(paymentMethod)"
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
          id="improvemyselfApp.paymentMethod.delete.question"
          data-cy="paymentMethodDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paymentMethod-heading" v-text="t$('improvemyselfApp.paymentMethod.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paymentMethod"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removePaymentMethod()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="paymentMethods && paymentMethods.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-method.component.ts"></script>
