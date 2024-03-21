<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="splitBookJoiner">
        <h2 class="jh-entity-heading" data-cy="splitBookJoinerDetailsHeading">
          <span v-text="t$('improvemyselfApp.splitBookJoiner.detail.title')"></span> {{ splitBookJoiner.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookJoiner.name')"></span>
          </dt>
          <dd>
            <span>{{ splitBookJoiner.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookJoiner.splitBook')"></span>
          </dt>
          <dd>
            <div v-if="splitBookJoiner.splitBook">
              <router-link :to="{ name: 'SplitBookView', params: { splitBookId: splitBookJoiner.splitBook.id } }">{{
                splitBookJoiner.splitBook.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="splitBookJoiner.id"
          :to="{ name: 'SplitBookJoinerEdit', params: { splitBookJoinerId: splitBookJoiner.id } }"
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

<script lang="ts" src="./split-book-joiner-details.component.ts"></script>
