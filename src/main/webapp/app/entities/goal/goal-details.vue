<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="goal">
        <h2 class="jh-entity-heading" data-cy="goalDetailsHeading">
          <span v-text="t$('improvemyselfApp.goal.detail.title')"></span> {{ goal.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.goal.title')"></span>
          </dt>
          <dd>
            <span>{{ goal.title }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.goal.description')"></span>
          </dt>
          <dd>
            <span>{{ goal.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.goal.priority')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.Priority.' + goal.priority)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.goal.ledger')"></span>
          </dt>
          <dd>
            <div v-if="goal.ledger">
              <router-link :to="{ name: 'LedgerView', params: { ledgerId: goal.ledger.id } }">{{ goal.ledger.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="goal.id" :to="{ name: 'GoalEdit', params: { goalId: goal.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./goal-details.component.ts"></script>
