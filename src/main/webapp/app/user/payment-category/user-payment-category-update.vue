<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.paymentCategory.home.createOrEditLabel"
          data-cy="PaymentCategoryCreateUpdateHeading"
          v-text="t$('improvemyselfApp.paymentCategory.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paymentCategory.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentCategory.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentCategory.code')" for="payment-category-code"></label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="payment-category-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model="v$.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentCategory.name')" for="payment-category-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="payment-category-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.paymentCategory.isDefault')"
              for="payment-category-isDefault"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="isDefault"
              id="payment-category-isDefault"
              data-cy="isDefault"
              :class="{ valid: !v$.isDefault.$invalid, invalid: v$.isDefault.$invalid }"
              v-model="v$.isDefault.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.paymentCategory.isHidden')"
              for="payment-category-isHidden"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="isHidden"
              id="payment-category-isHidden"
              data-cy="isHidden"
              :class="{ valid: !v$.isHidden.$invalid, invalid: v$.isHidden.$invalid }"
              v-model="v$.isHidden.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.paymentCategory.ledger')" for="payment-category-ledger"></label>
            <select class="form-control" id="payment-category-ledger" data-cy="ledger" name="ledger" v-model="paymentCategory.ledger">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  paymentCategory.ledger && ledgerOption.id === paymentCategory.ledger.id ? paymentCategory.ledger : ledgerOption
                "
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
<script lang="ts" src="./user-payment-category-update.component.ts"></script>
