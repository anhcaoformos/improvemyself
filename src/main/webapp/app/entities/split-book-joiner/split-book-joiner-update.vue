<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.splitBookJoiner.home.createOrEditLabel"
          data-cy="SplitBookJoinerCreateUpdateHeading"
          v-text="t$('improvemyselfApp.splitBookJoiner.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="splitBookJoiner.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="splitBookJoiner.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.splitBookJoiner.name')" for="split-book-joiner-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="split-book-joiner-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookJoiner.splitBook')"
              for="split-book-joiner-splitBook"
            ></label>
            <select
              class="form-control"
              id="split-book-joiner-splitBook"
              data-cy="splitBook"
              name="splitBook"
              v-model="splitBookJoiner.splitBook"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  splitBookJoiner.splitBook && splitBookOption.id === splitBookJoiner.splitBook.id
                    ? splitBookJoiner.splitBook
                    : splitBookOption
                "
                v-for="splitBookOption in splitBooks"
                :key="splitBookOption.id"
              >
                {{ splitBookOption.id }}
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
<script lang="ts" src="./split-book-joiner-update.component.ts"></script>
