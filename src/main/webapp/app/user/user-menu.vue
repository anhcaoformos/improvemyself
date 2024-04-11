<template>
  <div>
    <b-dropdown-item to="/user/ledger">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.ledger')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/transaction">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.transaction')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/goal">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.goal')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/payment-method">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.paymentMethod')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/payment-category">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.paymentCategory')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/objective">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.objective')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/split-book">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.splitBook')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/split-book-detail">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.splitBookDetail')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/split-book-joiner">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.splitBookJoiner')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user/note">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.note')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./user-menu.component.ts"></script>
