<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.transaction.home.createOrEditLabel"
          data-cy="TransactionCreateUpdateHeading"
          v-text="t$('improvemyselfApp.transaction.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="transaction.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="transaction.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.transaction.amount')" for="transaction-amount"></label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="transaction-amount"
              data-cy="amount"
              :class="{ valid: !v$.amount.$invalid, invalid: v$.amount.$invalid }"
              v-model.number="v$.amount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.transaction.description')" for="transaction-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="transaction-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.transaction.transactionDate')"
              for="transaction-transactionDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="transaction-transactionDate"
                  v-model="v$.transactionDate.$model"
                  name="transactionDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="transaction-transactionDate"
                data-cy="transactionDate"
                type="text"
                class="form-control"
                name="transactionDate"
                :class="{ valid: !v$.transactionDate.$invalid, invalid: v$.transactionDate.$invalid }"
                v-model="v$.transactionDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.transaction.transactionType')"
              for="transaction-transactionType"
            ></label>
            <select
              class="form-control"
              name="transactionType"
              :class="{ valid: !v$.transactionType.$invalid, invalid: v$.transactionType.$invalid }"
              v-model="v$.transactionType.$model"
              id="transaction-transactionType"
              data-cy="transactionType"
            >
              <option
                v-for="transactionType in transactionTypeValues"
                :key="transactionType"
                v-bind:value="transactionType"
                v-bind:label="t$('improvemyselfApp.TransactionType.' + transactionType)"
              >
                {{ transactionType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.transaction.objective')" for="transaction-objective"></label>
            <select class="form-control" id="transaction-objective" data-cy="objective" name="objective" v-model="transaction.objective">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  transaction.objective && objectiveOption.id === transaction.objective.id ? transaction.objective : objectiveOption
                "
                v-for="objectiveOption in objectives"
                :key="objectiveOption.id"
              >
                {{ objectiveOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.transaction.paymentMethod')"
              for="transaction-paymentMethod"
            ></label>
            <select
              class="form-control"
              id="transaction-paymentMethod"
              data-cy="paymentMethod"
              name="paymentMethod"
              v-model="transaction.paymentMethod"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  transaction.paymentMethod && paymentMethodOption.id === transaction.paymentMethod.id
                    ? transaction.paymentMethod
                    : paymentMethodOption
                "
                v-for="paymentMethodOption in paymentMethods"
                :key="paymentMethodOption.id"
              >
                {{ paymentMethodOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.transaction.targetPaymentMethod')"
              for="transaction-targetPaymentMethod"
            ></label>
            <select
              class="form-control"
              id="transaction-targetPaymentMethod"
              data-cy="paymentMethod"
              name="paymentMethod"
              v-model="transaction.paymentMethod"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  transaction.paymentMethod && paymentMethodOption.id === transaction.paymentMethod.id
                    ? transaction.paymentMethod
                    : paymentMethodOption
                "
                v-for="paymentMethodOption in paymentMethods"
                :key="paymentMethodOption.id"
              >
                {{ paymentMethodOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.transaction.paymentCategory')"
              for="transaction-paymentCategory"
            ></label>
            <select
              class="form-control"
              id="transaction-paymentCategory"
              data-cy="paymentCategory"
              name="paymentCategory"
              v-model="transaction.paymentCategory"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  transaction.paymentCategory && paymentCategoryOption.id === transaction.paymentCategory.id
                    ? transaction.paymentCategory
                    : paymentCategoryOption
                "
                v-for="paymentCategoryOption in paymentCategories"
                :key="paymentCategoryOption.id"
              >
                {{ paymentCategoryOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.transaction.ledger')" for="transaction-ledger"></label>
            <select class="form-control" id="transaction-ledger" data-cy="ledger" name="ledger" v-model="transaction.ledger">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="transaction.ledger && ledgerOption.id === transaction.ledger.id ? transaction.ledger : ledgerOption"
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
<script lang="ts" src="./transaction-update.component.ts"></script>
