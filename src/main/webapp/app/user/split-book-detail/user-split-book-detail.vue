<template>
  <div>
    <h2 id="page-heading" data-cy="SplitBookDetailHeading">
      <span v-text="t$('improvemyselfApp.splitBookDetail.home.title')" id="split-book-detail-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.splitBookDetail.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SplitBookDetailCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-split-book-detail"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.splitBookDetail.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && splitBookDetails && splitBookDetails.length === 0">
      <span v-text="t$('improvemyselfApp.splitBookDetail.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="splitBookDetails && splitBookDetails.length > 0">
      <table class="table table-striped" aria-describedby="splitBookDetails">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.amount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('personName')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.personName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'personName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('transactionDate')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.transactionDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'transactionDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('transactionType')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.transactionType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'transactionType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('groupId')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.groupId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'groupId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('joinerRole')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.joinerRole')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'joinerRole'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('splitBookJoiner.id')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.splitBookJoiner')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'splitBookJoiner.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('splitBook.id')">
              <span v-text="t$('improvemyselfApp.splitBookDetail.splitBook')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'splitBook.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="splitBookDetail in splitBookDetails" :key="splitBookDetail.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SplitBookDetailView', params: { splitBookDetailId: splitBookDetail.id } }">{{
                splitBookDetail.id
              }}</router-link>
            </td>
            <td>{{ splitBookDetail.amount }}</td>
            <td>{{ splitBookDetail.description }}</td>
            <td>{{ splitBookDetail.personName }}</td>
            <td>{{ splitBookDetail.transactionDate }}</td>
            <td v-text="t$('improvemyselfApp.TransactionType.' + splitBookDetail.transactionType)"></td>
            <td>{{ splitBookDetail.groupId }}</td>
            <td v-text="t$('improvemyselfApp.JoinerRole.' + splitBookDetail.joinerRole)"></td>
            <td>
              <div v-if="splitBookDetail.splitBookJoiner">
                <router-link :to="{ name: 'SplitBookJoinerView', params: { splitBookJoinerId: splitBookDetail.splitBookJoiner.id } }">{{
                  splitBookDetail.splitBookJoiner.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="splitBookDetail.splitBook">
                <router-link :to="{ name: 'SplitBookView', params: { splitBookId: splitBookDetail.splitBook.id } }">{{
                  splitBookDetail.splitBook.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SplitBookDetailView', params: { splitBookDetailId: splitBookDetail.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SplitBookDetailEdit', params: { splitBookDetailId: splitBookDetail.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(splitBookDetail)"
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
          id="improvemyselfApp.splitBookDetail.delete.question"
          data-cy="splitBookDetailDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-splitBookDetail-heading" v-text="t$('improvemyselfApp.splitBookDetail.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-splitBookDetail"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeSplitBookDetail()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="splitBookDetails && splitBookDetails.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-split-book-detail.component.ts"></script>
