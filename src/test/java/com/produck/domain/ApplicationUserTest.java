package com.produck.domain;

import static com.produck.domain.ApplicationUserTestSamples.*;
import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.NoteTestSamples.*;
import static com.produck.domain.SplitBookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void hashCodeVerifier() throws Exception {
        ApplicationUser applicationUser = new ApplicationUser();
        assertThat(applicationUser.hashCode()).isZero();

        ApplicationUser applicationUser1 = getApplicationUserSample1();
        applicationUser.setId(applicationUser1.getId());
        assertThat(applicationUser).hasSameHashCodeAs(applicationUser1);
    }

    @Test
    void splitBookTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        SplitBook splitBookBack = getSplitBookRandomSampleGenerator();

        applicationUser.addSplitBook(splitBookBack);
        assertThat(applicationUser.getSplitBooks()).containsOnly(splitBookBack);
        assertThat(splitBookBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.removeSplitBook(splitBookBack);
        assertThat(applicationUser.getSplitBooks()).doesNotContain(splitBookBack);
        assertThat(splitBookBack.getApplicationUser()).isNull();

        applicationUser.splitBooks(new HashSet<>(Set.of(splitBookBack)));
        assertThat(applicationUser.getSplitBooks()).containsOnly(splitBookBack);
        assertThat(splitBookBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.setSplitBooks(new HashSet<>());
        assertThat(applicationUser.getSplitBooks()).doesNotContain(splitBookBack);
        assertThat(splitBookBack.getApplicationUser()).isNull();
    }

    @Test
    void noteTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Note noteBack = getNoteRandomSampleGenerator();

        applicationUser.addNote(noteBack);
        assertThat(applicationUser.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.removeNote(noteBack);
        assertThat(applicationUser.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getApplicationUser()).isNull();

        applicationUser.notes(new HashSet<>(Set.of(noteBack)));
        assertThat(applicationUser.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.setNotes(new HashSet<>());
        assertThat(applicationUser.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getApplicationUser()).isNull();
    }

    @Test
    void ledgerTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        applicationUser.addLedger(ledgerBack);
        assertThat(applicationUser.getLedgers()).containsOnly(ledgerBack);
        assertThat(ledgerBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.removeLedger(ledgerBack);
        assertThat(applicationUser.getLedgers()).doesNotContain(ledgerBack);
        assertThat(ledgerBack.getApplicationUser()).isNull();

        applicationUser.ledgers(new HashSet<>(Set.of(ledgerBack)));
        assertThat(applicationUser.getLedgers()).containsOnly(ledgerBack);
        assertThat(ledgerBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.setLedgers(new HashSet<>());
        assertThat(applicationUser.getLedgers()).doesNotContain(ledgerBack);
        assertThat(ledgerBack.getApplicationUser()).isNull();
    }
}
