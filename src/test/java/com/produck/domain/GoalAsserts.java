package com.produck.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class GoalAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGoalAllPropertiesEquals(Goal expected, Goal actual) {
        assertGoalAutoGeneratedPropertiesEquals(expected, actual);
        assertGoalAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGoalAllUpdatablePropertiesEquals(Goal expected, Goal actual) {
        assertGoalUpdatableFieldsEquals(expected, actual);
        assertGoalUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGoalAutoGeneratedPropertiesEquals(Goal expected, Goal actual) {
        assertThat(expected)
            .as("Verify Goal auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGoalUpdatableFieldsEquals(Goal expected, Goal actual) {
        assertThat(expected)
            .as("Verify Goal relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getPriority()).as("check priority").isEqualTo(actual.getPriority()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGoalUpdatableRelationshipsEquals(Goal expected, Goal actual) {
        assertThat(expected)
            .as("Verify Goal relationships")
            .satisfies(e -> assertThat(e.getLedger()).as("check ledger").isEqualTo(actual.getLedger()));
    }
}
