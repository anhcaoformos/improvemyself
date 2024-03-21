package com.produck.domain;

import static com.produck.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAllPropertiesEquals(Transaction expected, Transaction actual) {
        assertTransactionAutoGeneratedPropertiesEquals(expected, actual);
        assertTransactionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAllUpdatablePropertiesEquals(Transaction expected, Transaction actual) {
        assertTransactionUpdatableFieldsEquals(expected, actual);
        assertTransactionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionAutoGeneratedPropertiesEquals(Transaction expected, Transaction actual) {
        assertThat(expected)
            .as("Verify Transaction auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionUpdatableFieldsEquals(Transaction expected, Transaction actual) {
        assertThat(expected)
            .as("Verify Transaction relevant properties")
            .satisfies(e -> assertThat(e.getAmount()).as("check amount").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getAmount()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getTransactionDate()).as("check transactionDate").isEqualTo(actual.getTransactionDate()))
            .satisfies(e -> assertThat(e.getTransactionType()).as("check transactionType").isEqualTo(actual.getTransactionType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionUpdatableRelationshipsEquals(Transaction expected, Transaction actual) {
        assertThat(expected)
            .as("Verify Transaction relationships")
            .satisfies(e -> assertThat(e.getObjective()).as("check objective").isEqualTo(actual.getObjective()))
            .satisfies(e -> assertThat(e.getPaymentMethod()).as("check paymentMethod").isEqualTo(actual.getPaymentMethod()))
            .satisfies(e -> assertThat(e.getPaymentCategory()).as("check paymentCategory").isEqualTo(actual.getPaymentCategory()))
            .satisfies(e -> assertThat(e.getLedger()).as("check ledger").isEqualTo(actual.getLedger()));
    }
}