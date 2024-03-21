<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.paymentMethod.home.createOrEditLabel"
          data-cy="PaymentMethodCreateUpdateHeading"
          v-text="t$('improvemyselfApp.paymentMethod.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paymentMethod.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentMethod.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentMethod.code')" for="payment-method-code"></label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="payment-method-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model="v$.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentMethod.name')" for="payment-method-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="payment-method-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentMethod.type')" for="payment-method-type"></label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="payment-method-type"
              data-cy="type"
            >
              <option
                v-for="paymentMethodType in paymentMethodTypeValues"
                :key="paymentMethodType"
                v-bind:value="paymentMethodType"
                v-bind:label="t$('improvemyselfApp.PaymentMethodType.' + paymentMethodType)"
              >
                {{ paymentMethodType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentMethod.isHidden')" for="payment-method-isHidden"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isHidden"
              id="payment-method-isHidden"
              data-cy="isHidden"
              :class="{ valid: !v$.isHidden.$invalid, invalid: v$.isHidden.$invalid }"
              v-model="v$.isHidden.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentMethod.ledger')" for="payment-method-ledger"></label>
            <select class="form-control" id="payment-method-ledger" data-cy="ledger" name="ledger" v-model="paymentMethod.ledger">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="paymentMethod.ledger && ledgerOption.id === paymentMethod.ledger.id ? paymentMethod.ledger : ledgerOption"
                v-for="ledgerOption in ledgers"
                :key="ledgerOption.id"
              >
                {{ ledgerOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./payment-method-update.component.ts"></script>
