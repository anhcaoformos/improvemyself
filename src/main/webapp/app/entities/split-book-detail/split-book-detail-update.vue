<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.splitBookDetail.home.createOrEditLabel"
          data-cy="SplitBookDetailCreateUpdateHeading"
          v-text="t$('improvemyselfApp.splitBookDetail.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="splitBookDetail.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="splitBookDetail.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.splitBookDetail.amount')" for="split-book-detail-amount"></label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="split-book-detail-amount"
              data-cy="amount"
              :class="{ valid: !v$.amount.$invalid, invalid: v$.amount.$invalid }"
              v-model.number="v$.amount.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.description')"
              for="split-book-detail-description"
            ></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="split-book-detail-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.personName')"
              for="split-book-detail-personName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="personName"
              id="split-book-detail-personName"
              data-cy="personName"
              :class="{ valid: !v$.personName.$invalid, invalid: v$.personName.$invalid }"
              v-model="v$.personName.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.transactionDate')"
              for="split-book-detail-transactionDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="split-book-detail-transactionDate"
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
                id="split-book-detail-transactionDate"
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
              v-text="t$('improvemyselfApp.splitBookDetail.transactionType')"
              for="split-book-detail-transactionType"
            ></label>
            <select
              class="form-control"
              name="transactionType"
              :class="{ valid: !v$.transactionType.$invalid, invalid: v$.transactionType.$invalid }"
              v-model="v$.transactionType.$model"
              id="split-book-detail-transactionType"
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
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.groupId')"
              for="split-book-detail-groupId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="groupId"
              id="split-book-detail-groupId"
              data-cy="groupId"
              :class="{ valid: !v$.groupId.$invalid, invalid: v$.groupId.$invalid }"
              v-model="v$.groupId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.joinerRole')"
              for="split-book-detail-joinerRole"
            ></label>
            <select
              class="form-control"
              name="joinerRole"
              :class="{ valid: !v$.joinerRole.$invalid, invalid: v$.joinerRole.$invalid }"
              v-model="v$.joinerRole.$model"
              id="split-book-detail-joinerRole"
              data-cy="joinerRole"
            >
              <option
                v-for="joinerRole in joinerRoleValues"
                :key="joinerRole"
                v-bind:value="joinerRole"
                v-bind:label="t$('improvemyselfApp.JoinerRole.' + joinerRole)"
              >
                {{ joinerRole }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.splitBookJoiner')"
              for="split-book-detail-splitBookJoiner"
            ></label>
            <select
              class="form-control"
              id="split-book-detail-splitBookJoiner"
              data-cy="splitBookJoiner"
              name="splitBookJoiner"
              v-model="splitBookDetail.splitBookJoiner"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  splitBookDetail.splitBookJoiner && splitBookJoinerOption.id === splitBookDetail.splitBookJoiner.id
                    ? splitBookDetail.splitBookJoiner
                    : splitBookJoinerOption
                "
                v-for="splitBookJoinerOption in splitBookJoiners"
                :key="splitBookJoinerOption.id"
              >
                {{ splitBookJoinerOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('improvemyselfApp.splitBookDetail.splitBook')"
              for="split-book-detail-splitBook"
            ></label>
            <select
              class="form-control"
              id="split-book-detail-splitBook"
              data-cy="splitBook"
              name="splitBook"
              v-model="splitBookDetail.splitBook"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  splitBookDetail.splitBook && splitBookOption.id === splitBookDetail.splitBook.id
                    ? splitBookDetail.splitBook
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
<script lang="ts" src="./split-book-detail-update.component.ts"></script>
