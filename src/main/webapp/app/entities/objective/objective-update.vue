<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.objective.home.createOrEditLabel"
          data-cy="ObjectiveCreateUpdateHeading"
          v-text="t$('improvemyselfApp.objective.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="objective.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="objective.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.objective.name')" for="objective-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="objective-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.objective.type')" for="objective-type"></label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="objective-type"
              data-cy="type"
            >
              <option
                v-for="objectiveType in objectiveTypeValues"
                :key="objectiveType"
                v-bind:value="objectiveType"
                v-bind:label="t$('improvemyselfApp.ObjectiveType.' + objectiveType)"
              >
                {{ objectiveType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.objective.isHidden')" for="objective-isHidden"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isHidden"
              id="objective-isHidden"
              data-cy="isHidden"
              :class="{ valid: !v$.isHidden.$invalid, invalid: v$.isHidden.$invalid }"
              v-model="v$.isHidden.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.objective.paymentCategory')"
              for="objective-paymentCategory"
            ></label>
            <select
              class="form-control"
              id="objective-paymentCategory"
              data-cy="paymentCategory"
              name="paymentCategory"
              v-model="objective.paymentCategory"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  objective.paymentCategory && paymentCategoryOption.id === objective.paymentCategory.id
                    ? objective.paymentCategory
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
            <label class="form-control-label" v-text="t$('improvemyselfApp.objective.ledger')" for="objective-ledger"></label>
            <select class="form-control" id="objective-ledger" data-cy="ledger" name="ledger" v-model="objective.ledger">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="objective.ledger && ledgerOption.id === objective.ledger.id ? objective.ledger : ledgerOption"
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
<script lang="ts" src="./objective-update.component.ts"></script>
