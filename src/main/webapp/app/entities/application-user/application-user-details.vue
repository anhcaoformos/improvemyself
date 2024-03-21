<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="applicationUser">
        <h2 class="jh-entity-heading" data-cy="applicationUserDetailsHeading">
          <span v-text="t$('improvemyselfApp.applicationUser.detail.title')"></span> {{ applicationUser.id }}
        </h2>
        <dl class="row jh-entity-details"></dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="applicationUser.id"
          :to="{ name: 'ApplicationUserEdit', params: { applicationUserId: applicationUser.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-user-details.component.ts"></script>
