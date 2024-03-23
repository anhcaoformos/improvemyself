<template>
  <div>
    <h2 id="page-heading" data-cy="NoteHeading">
      <span v-text="t$('improvemyselfApp.note.home.title')" id="note-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('improvemyselfApp.note.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'NoteCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-note">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('improvemyselfApp.note.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && notes && notes.length === 0">
      <span v-text="t$('improvemyselfApp.note.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="notes && notes.length > 0">
      <table class="table table-striped" aria-describedby="notes">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="t$('improvemyselfApp.note.title')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="t$('improvemyselfApp.note.description')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('noteDateFrom')">
              <span v-text="t$('improvemyselfApp.note.noteDateFrom')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'noteDateFrom'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('noteDateTo')">
              <span v-text="t$('improvemyselfApp.note.noteDateTo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'noteDateTo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('noteType')">
              <span v-text="t$('improvemyselfApp.note.noteType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'noteType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('repeatType')">
              <span v-text="t$('improvemyselfApp.note.repeatType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'repeatType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('alertType')">
              <span v-text="t$('improvemyselfApp.note.alertType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alertType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('user.id')">
              <span v-text="t$('improvemyselfApp.note.user')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="note in notes" :key="note.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'NoteView', params: { noteId: note.id } }">{{ note.id }}</router-link>
            </td>
            <td>{{ note.title }}</td>
            <td>{{ note.description }}</td>
            <td>{{ note.noteDateFrom }}</td>
            <td>{{ note.noteDateTo }}</td>
            <td v-text="t$('improvemyselfApp.NoteType.' + note.noteType)"></td>
            <td v-text="t$('improvemyselfApp.RepeatType.' + note.repeatType)"></td>
            <td v-text="t$('improvemyselfApp.AlertType.' + note.alertType)"></td>
            <td>
              <div v-if="note.user">
                <router-link :to="{ name: 'UserView', params: { userId: note.user.id } }">{{ note.user.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'NoteView', params: { noteId: note.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'NoteEdit', params: { noteId: note.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(note)"
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
        <span id="improvemyselfApp.note.delete.question" data-cy="noteDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-note-heading" v-text="t$('improvemyselfApp.note.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-note"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeNote()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="notes && notes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./note.component.ts"></script>
