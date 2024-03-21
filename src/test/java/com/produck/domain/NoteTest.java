package com.produck.domain;

import static com.produck.domain.ApplicationUserTestSamples.*;
import static com.produck.domain.NoteTestSamples.*;
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
    void applicationUserTest() throws Exception {
        Note note = getNoteRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        note.setApplicationUser(applicationUserBack);
        assertThat(note.getApplicationUser()).isEqualTo(applicationUserBack);

        note.applicationUser(null);
        assertThat(note.getApplicationUser()).isNull();
    }
}