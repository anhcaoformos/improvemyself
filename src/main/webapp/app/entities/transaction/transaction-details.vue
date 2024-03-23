<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="transaction">
        <h2 class="jh-entity-heading" data-cy="transactionDetailsHeading">
          <span v-text="t$('improvemyselfApp.transaction.detail.title')"></span> {{ transaction.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.amount')"></span>
          </dt>
          <dd>
            <span>{{ transaction.amount }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.description')"></span>
          </dt>
          <dd>
            <span>{{ transaction.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.transactionDate')"></span>
          </dt>
          <dd>
            <span>{{ transaction.transactionDate }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.transactionType')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.TransactionType.' + transaction.transactionType)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.objective')"></span>
          </dt>
          <dd>
            <div v-if="transaction.objective">
              <router-link :to="{ name: 'ObjectiveView', params: { objectiveId: transaction.objective.id } }">{{
                transaction.objective.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.paymentMethod')"></span>
          </dt>
          <dd>
            <div v-if="transaction.paymentMethod">
              <router-link :to="{ name: 'PaymentMethodView', params: { paymentMethodId: transaction.paymentMethod.id } }">{{
                transaction.paymentMethod.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.targetPaymentMethod')"></span>
          </dt>
          <dd>
            <div v-if="transaction.targetPaymentMethod">
              <router-link
                :to="{ name: 'TargetPaymentMethodView', params: { targetPaymentMethodId: transaction.targetPaymentMethod.id } }"
                >{{ transaction.targetPaymentMethod.id }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.paymentCategory')"></span>
          </dt>
          <dd>
            <div v-if="transaction.paymentCategory">
              <router-link :to="{ name: 'PaymentCategoryView', params: { paymentCategoryId: transaction.paymentCategory.id } }">{{
                transaction.paymentCategory.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.transaction.ledger')"></span>
          </dt>
          <dd>
            <div v-if="transaction.ledger">
              <router-link :to="{ name: 'LedgerView', params: { ledgerId: transaction.ledger.id } }">{{
                transaction.ledger.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="transaction.id"
          :to="{ name: 'TransactionEdit', params: { transactionId: transaction.id } }"
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

<script lang="ts" src="./transaction-details.component.ts"></script>
