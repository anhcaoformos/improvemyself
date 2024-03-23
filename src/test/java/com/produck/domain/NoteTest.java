package com.produck.domain;

import static com.produck.domain.NoteTestSamples.*;
import static com.produck.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = getNoteSample1();
        Note note2 = new Note();
        assertThat(note1).isNotEqualTo(note2);

        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);

        note2 = getNoteSample2();
        assertThat(note1).isNotEqualTo(note2);
    }

    @Test
    void userTest() throws Exception {
        Note note = getNoteRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        note.setUser(userBack);
        assertThat(note.getUser()).isEqualTo(userBack);

        note.user(null);
        assertThat(note.getUser()).isNull();
    }
}
