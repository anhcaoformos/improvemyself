<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="ledger">
        <h2 class="jh-entity-heading" data-cy="ledgerDetailsHeading">
          <span v-text="t$('improvemyselfApp.ledger.detail.title')"></span> {{ ledger.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.ledger.name')"></span>
          </dt>
          <dd>
            <span>{{ ledger.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.ledger.isDefault')"></span>
          </dt>
          <dd>
            <span>{{ ledger.isDefault }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.ledger.user')"></span>
          </dt>
          <dd>
            <div v-if="ledger.user">
              <router-link :to="{ name: 'UserView', params: { userId: ledger.user.id } }">{{ ledger.user.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="ledger.id" :to="{ name: 'LedgerEdit', params: { ledgerId: ledger.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./ledger-details.component.ts"></script>
