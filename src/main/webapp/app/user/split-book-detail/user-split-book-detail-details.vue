<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="splitBookDetail">
        <h2 class="jh-entity-heading" data-cy="splitBookDetailDetailsHeading">
          <span v-text="t$('improvemyselfApp.splitBookDetail.detail.title')"></span> {{ splitBookDetail.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.amount')"></span>
          </dt>
          <dd>
            <span>{{ splitBookDetail.amount }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.description')"></span>
          </dt>
          <dd>
            <span>{{ splitBookDetail.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.personName')"></span>
          </dt>
          <dd>
            <span>{{ splitBookDetail.personName }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.transactionDate')"></span>
          </dt>
          <dd>
            <span>{{ splitBookDetail.transactionDate }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.transactionType')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.TransactionType.' + splitBookDetail.transactionType)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.groupId')"></span>
          </dt>
          <dd>
            <span>{{ splitBookDetail.groupId }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.joinerRole')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.JoinerRole.' + splitBookDetail.joinerRole)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.splitBookJoiner')"></span>
          </dt>
          <dd>
            <div v-if="splitBookDetail.splitBookJoiner">
              <router-link :to="{ name: 'SplitBookJoinerView', params: { splitBookJoinerId: splitBookDetail.splitBookJoiner.id } }">{{
                splitBookDetail.splitBookJoiner.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.splitBookDetail.splitBook')"></span>
          </dt>
          <dd>
            <div v-if="splitBookDetail.splitBook">
              <router-link :to="{ name: 'SplitBookView', params: { splitBookId: splitBookDetail.splitBook.id } }">{{
                splitBookDetail.splitBook.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="splitBookDetail.id"
          :to="{ name: 'SplitBookDetailEdit', params: { splitBookDetailId: splitBookDetail.id } }"
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

<script lang="ts" src="./user-split-book-detail-details.component.ts"></script>
