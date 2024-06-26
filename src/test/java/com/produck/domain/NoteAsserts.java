package com.produck.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NoteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNoteAllPropertiesEquals(Note expected, Note actual) {
        assertNoteAutoGeneratedPropertiesEquals(expected, actual);
        assertNoteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNoteAllUpdatablePropertiesEquals(Note expected, Note actual) {
        assertNoteUpdatableFieldsEquals(expected, actual);
        assertNoteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNoteAutoGeneratedPropertiesEquals(Note expected, Note actual) {
        assertThat(expected)
            .as("Verify Note auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNoteUpdatableFieldsEquals(Note expected, Note actual) {
        assertThat(expected)
            .as("Verify Note relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getNoteDateFrom()).as("check noteDateFrom").isEqualTo(actual.getNoteDateFrom()))
            .satisfies(e -> assertThat(e.getNoteDateTo()).as("check noteDateTo").isEqualTo(actual.getNoteDateTo()))
            .satisfies(e -> assertThat(e.getNoteType()).as("check noteType").isEqualTo(actual.getNoteType()))
            .satisfies(e -> assertThat(e.getRepeatType()).as("check repeatType").isEqualTo(actual.getRepeatType()))
            .satisfies(e -> assertThat(e.getAlertType()).as("check alertType").isEqualTo(actual.getAlertType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNoteUpdatableRelationshipsEquals(Note expected, Note actual) {
        assertThat(expected)
            .as("Verify Note relationships")
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()));
    }
}
