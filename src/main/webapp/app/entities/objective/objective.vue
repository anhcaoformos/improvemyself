<template>
  <div>
    <h2 id="page-heading" data-cy="ObjectiveHeading">
      <span v-text="t$('improvemyselfApp.objective.home.title')" id="objective-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.objective.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ObjectiveCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-objective"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.objective.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && objectives && objectives.length === 0">
      <span v-text="t$('improvemyselfApp.objective.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="objectives && objectives.length > 0">
      <table class="table table-striped" aria-describedby="objectives">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="t$('improvemyselfApp.objective.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('type')">
              <span v-text="t$('improvemyselfApp.objective.type')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isHidden')">
              <span v-text="t$('improvemyselfApp.objective.isHidden')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isHidden'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentCategory.id')">
              <span v-text="t$('improvemyselfApp.objective.paymentCategory')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentCategory.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ledger.id')">
              <span v-text="t$('improvemyselfApp.objective.ledger')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ledger.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="objective in objectives" :key="objective.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ObjectiveView', params: { objectiveId: objective.id } }">{{ objective.id }}</router-link>
            </td>
            <td>{{ objective.name }}</td>
            <td v-text="t$('improvemyselfApp.ObjectiveType.' + objective.type)"></td>
            <td>{{ objective.isHidden }}</td>
            <td>
              <div v-if="objective.paymentCategory">
                <router-link :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: objective.paymentCategory.id } }">{{
                  objective.paymentCategory.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="objective.ledger">
                <router-link :to="{ name: 'LedgerView', params: { ledgerId: objective.ledger.id } }">{{ objective.ledger.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ObjectiveView', params: { objectiveId: objective.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ObjectiveEdit', params: { objectiveId: objective.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(objective)"
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
          id="improvemyselfApp.objective.delete.question"
          data-cy="objectiveDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-objective-heading" v-text="t$('improvemyselfApp.objective.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-objective"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeObjective()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="objectives && objectives.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./objective.component.ts"></script>
