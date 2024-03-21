<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="objective">
        <h2 class="jh-entity-heading" data-cy="objectiveDetailsHeading">
          <span v-text="t$('improvemyselfApp.objective.detail.title')"></span> {{ objective.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.objective.name')"></span>
          </dt>
          <dd>
            <span>{{ objective.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.objective.type')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.ObjectiveType.' + objective.type)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.objective.isHidden')"></span>
          </dt>
          <dd>
            <span>{{ objective.isHidden }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.objective.paymentCategory')"></span>
          </dt>
          <dd>
            <div v-if="objective.paymentCategory">
              <router-link :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: objective.paymentCategory.id } }">{{
                objective.paymentCategory.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.objective.ledger')"></span>
          </dt>
          <dd>
            <div v-if="objective.ledger">
              <router-link :to="{ name: 'LedgerView', params: { ledgerId: objective.ledger.id } }">{{ objective.ledger.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="objective.id"
          :to="{ name: 'ObjectiveEdit', params: { objectiveId: objective.id } }"
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

<script lang="ts" src="./objective-details.component.ts"></script>
