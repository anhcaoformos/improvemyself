<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.goal.home.createOrEditLabel"
          data-cy="GoalCreateUpdateHeading"
          v-text="t$('improvemyselfApp.goal.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="goal.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="goal.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.goal.title')" for="goal-title"></label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="goal-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.goal.description')" for="goal-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="goal-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.goal.priority')" for="goal-priority"></label>
            <select
              class="form-control"
              name="priority"
              :class="{ valid: !v$.priority.$invalid, invalid: v$.priority.$invalid }"
              v-model="v$.priority.$model"
              id="goal-priority"
              data-cy="priority"
            >
              <option
                v-for="priority in priorityValues"
                :key="priority"
                v-bind:value="priority"
                v-bind:label="t$('improvemyselfApp.Priority.' + priority)"
              >
                {{ priority }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.goal.ledger')" for="goal-ledger"></label>
            <select class="form-control" id="goal-ledger" data-cy="ledger" name="ledger" v-model="goal.ledger">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="goal.ledger && ledgerOption.id === goal.ledger.id ? goal.ledger : ledgerOption"
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
<script lang="ts" src="./goal-update.component.ts"></script>
