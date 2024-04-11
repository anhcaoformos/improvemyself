<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentMethod">
        <h2 class="jh-entity-heading" data-cy="paymentMethodDetailsHeading">
          <span v-text="t$('improvemyselfApp.paymentMethod.detail.title')"></span> {{ paymentMethod.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.paymentMethod.code')"></span>
          </dt>
          <dd>
            <span>{{ paymentMethod.code }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentMethod.name')"></span>
          </dt>
          <dd>
            <span>{{ paymentMethod.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentMethod.type')"></span>
          </dt>
          <dd>
            <span v-text="t$('improvemyselfApp.PaymentMethodType.' + paymentMethod.type)"></span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentMethod.isHidden')"></span>
          </dt>
          <dd>
            <span>{{ paymentMethod.isHidden }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentMethod.ledger')"></span>
          </dt>
          <dd>
            <div v-if="paymentMethod.ledger">
              <router-link :to="{ name: 'LedgerView', params: { ledgerId: paymentMethod.ledger.id } }">{{
                paymentMethod.ledger.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="paymentMethod.id"
          :to="{ name: 'PaymentMethodEdit', params: { paymentMethodId: paymentMethod.id } }"
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

<script lang="ts" src="./user-payment-method-details.component.ts"></script>
