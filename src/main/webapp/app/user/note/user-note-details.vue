<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="note">
        <h2 class="jh-entity-heading" data-cy="noteDetailsHeading">
          <span v-text="t$('improvemyselfApp.note.detail.title')"></span> {{ note.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.note.title')"></span>
          </dt>
          <dd>
            <span>{{ note.title }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.description')"></span>
          </dt>
          <dd>
            <span>{{ note.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.noteDateFrom')"></span>
          </dt>
          <dd>
            <span>{{ note.noteDateFrom }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.noteDateTo')"></span>
          </dt>
          <dd>
            <span>{{ note.noteDateTo }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.noteType')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.NoteType.' + note.noteType)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.repeatType')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.RepeatType.' + note.repeatType)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.alertType')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.AlertType.' + note.alertType)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.note.user')"></span>
          </dt>
          <dd>
            <div v-if="note.user">
              <router-link :to="{ name: 'JhiUserView', params: { userId: note.user.id } }">{{ note.user.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="note.id" :to="{ name: 'NoteEdit', params: { noteId: note.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-note-details.component.ts"></script>
