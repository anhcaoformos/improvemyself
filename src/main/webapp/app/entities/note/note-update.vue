<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="improvemyselfApp.note.home.createOrEditLabel"
          data-cy="NoteCreateUpdateHeading"
          v-text="t$('improvemyselfApp.note.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="note.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="note.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.title')" for="note-title"></label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="note-title"
              data-cy="title"
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.description')" for="note-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="note-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.noteDateFrom')" for="note-noteDateFrom"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="note-noteDateFrom"
                  v-model="v$.noteDateFrom.$model"
                  name="noteDateFrom"
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
                id="note-noteDateFrom"
                data-cy="noteDateFrom"
                type="text"
                class="form-control"
                name="noteDateFrom"
                :class="{ valid: !v$.noteDateFrom.$invalid, invalid: v$.noteDateFrom.$invalid }"
                v-model="v$.noteDateFrom.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.noteDateTo')" for="note-noteDateTo"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="note-noteDateTo"
                  v-model="v$.noteDateTo.$model"
                  name="noteDateTo"
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
                id="note-noteDateTo"
                data-cy="noteDateTo"
                type="text"
                class="form-control"
                name="noteDateTo"
                :class="{ valid: !v$.noteDateTo.$invalid, invalid: v$.noteDateTo.$invalid }"
                v-model="v$.noteDateTo.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.noteType')" for="note-noteType"></label>
            <select
              class="form-control"
              name="noteType"
              :class="{ valid: !v$.noteType.$invalid, invalid: v$.noteType.$invalid }"
              v-model="v$.noteType.$model"
              id="note-noteType"
              data-cy="noteType"
            >
              <option
                v-for="noteType in noteTypeValues"
                :key="noteType"
                v-bind:value="noteType"
                v-bind:label="t$('improvemyselfApp.NoteType.' + noteType)"
              >
                {{ noteType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.repeatType')" for="note-repeatType"></label>
            <select
              class="form-control"
              name="repeatType"
              :class="{ valid: !v$.repeatType.$invalid, invalid: v$.repeatType.$invalid }"
              v-model="v$.repeatType.$model"
              id="note-repeatType"
              data-cy="repeatType"
            >
              <option
                v-for="repeatType in repeatTypeValues"
                :key="repeatType"
                v-bind:value="repeatType"
                v-bind:label="t$('improvemyselfApp.RepeatType.' + repeatType)"
              >
                {{ repeatType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.alertType')" for="note-alertType"></label>
            <select
              class="form-control"
              name="alertType"
              :class="{ valid: !v$.alertType.$invalid, invalid: v$.alertType.$invalid }"
              v-model="v$.alertType.$model"
              id="note-alertType"
              data-cy="alertType"
            >
              <option
                v-for="alertType in alertTypeValues"
                :key="alertType"
                v-bind:value="alertType"
                v-bind:label="t$('improvemyselfApp.AlertType.' + alertType)"
              >
                {{ alertType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('improvemyselfApp.note.applicationUser')" for="note-applicationUser"></label>
            <select
              class="form-control"
              id="note-applicationUser"
              data-cy="applicationUser"
              name="applicationUser"
              v-model="note.applicationUser"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  note.applicationUser && applicationUserOption.id === note.applicationUser.id
                    ? note.applicationUser
                    : applicationUserOption
                "
                v-for="applicationUserOption in applicationUsers"
                :key="applicationUserOption.id"
              >
                {{ applicationUserOption.id }}
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
<script lang="ts" src="./note-update.component.ts"></script>
