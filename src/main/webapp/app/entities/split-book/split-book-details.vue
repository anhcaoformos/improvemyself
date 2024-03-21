<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="splitBook">
        <h2 class="jh-entity-heading" data-cy="splitBookDetailsHeading">
          <span v-text="t$('improvemyselfApp.splitBook.detail.title')"></span> {{ splitBook.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.splitBook.description')"></span>
          </dt>
          <dd>
            <span>{{ splitBook.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBook.name')"></span>
          </dt>
          <dd>
            <span>{{ splitBook.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBook.applicationUser')"></span>
          </dt>
          <dd>
            <div v-if="splitBook.applicationUser">
              <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: splitBook.applicationUser.id } }">{{
                splitBook.applicationUser.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="splitBook.id"
          :to="{ name: 'SplitBookEdit', params: { splitBookId: splitBook.id } }"
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

<script lang="ts" src="./split-book-details.component.ts"></script>
