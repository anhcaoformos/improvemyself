<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.splitBook.home.createOrEditLabel"
          data-cy="SplitBookCreateUpdateHeading"
          v-text="t$('improvemyselfApp.splitBook.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="splitBook.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="splitBook.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.splitBook.description')" for="split-book-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="split-book-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.splitBook.name')" for="split-book-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="split-book-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.splitBook.user')" for="split-book-user"></label>
            <select class="form-control" id="split-book-user" data-cy="user" name="user" v-model="splitBook.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="splitBook.user && userOption.id === splitBook.user.id ? splitBook.user : userOption"
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
<script lang="ts" src="./user-split-book-update.component.ts"></script>
