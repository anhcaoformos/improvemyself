<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentCategory">
        <h2 class="jh-entity-heading" data-cy="paymentCategoryDetailsHeading">
          <span v-text="t$('improvemyselfApp.paymentCategory.detail.title')"></span> {{ paymentCategory.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('improvemyselfApp.paymentCategory.code')"></span>
          </dt>
          <dd>
            <span>{{ paymentCategory.code }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentCategory.name')"></span>
          </dt>
          <dd>
            <span>{{ paymentCategory.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentCategory.isDefault')"></span>
          </dt>
          <dd>
            <span>{{ paymentCategory.isDefault }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentCategory.isHidden')"></span>
          </dt>
          <dd>
            <span>{{ paymentCategory.isHidden }}</span>
          </dd>
          <dt>
            <span v-text="t$('improvemyselfApp.paymentCategory.ledger')"></span>
          </dt>
          <dd>
            <div v-if="paymentCategory.ledger">
              <router-link :to="{ name: 'LedgerView', params: { ledgerId: paymentCategory.ledger.id } }">{{
                paymentCategory.ledger.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="paymentCategory.id"
          :to="{ name: 'PaymentCategoryEdit', params: { paymentCategoryId: paymentCategory.id } }"
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

<script lang="ts" src="./payment-category-details.component.ts"></script>
