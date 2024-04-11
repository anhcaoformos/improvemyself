<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.ledger.home.createOrEditLabel"
          data-cy="LedgerCreateUpdateHeading"
          v-text="t$('improvemyselfApp.ledger.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="ledger.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="ledger.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.ledger.name')" for="ledger-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="ledger-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.ledger.isDefault')" for="ledger-isDefault"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isDefault"
              id="ledger-isDefault"
              data-cy="isDefault"
              :class="{ valid: !v$.isDefault.$invalid, invalid: v$.isDefault.$invalid }"
              v-model="v$.isDefault.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.ledger.user')" for="ledger-user"></label>
            <select class="form-control" id="ledger-user" data-cy="user" name="user" v-model="ledger.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="ledger.user && userOption.id === ledger.user.id ? ledger.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
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
<script lang="ts" src="./user-ledger-update.component.ts"></script>
