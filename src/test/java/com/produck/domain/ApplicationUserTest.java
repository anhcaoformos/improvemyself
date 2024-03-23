package com.produck.domain;

import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.NoteTestSamples.*;
import static com.produck.domain.SplitBookTestSamples.*;
import static com.produck.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = getUserSample1();
        User user2 = new User();
        assertThat(user1).isNotEqualTo(user2);

        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);

        user2 = getUserSample2();
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void hashCodeVerifier() throws Exception {
        User user = new User();
        assertThat(user.hashCode()).isZero();

        User user1 = getUserSample1();
        user.setId(user1.getId());
        assertThat(user).hasSameHashCodeAs(user1);
    }

    @Test
    void splitBookTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        SplitBook splitBookBack = getSplitBookRandomSampleGenerator();

        user.addSplitBook(splitBookBack);
        assertThat(user.getSplitBooks()).containsOnly(splitBookBack);
        assertThat(splitBookBack.getUser()).isEqualTo(user);

        user.removeSplitBook(splitBookBack);
        assertThat(user.getSplitBooks()).doesNotContain(splitBookBack);
        assertThat(splitBookBack.getUser()).isNull();

        user.splitBooks(new HashSet<>(Set.of(splitBookBack)));
        assertThat(user.getSplitBooks()).containsOnly(splitBookBack);
        assertThat(splitBookBack.getUser()).isEqualTo(user);

        user.setSplitBooks(new HashSet<>());
        assertThat(user.getSplitBooks()).doesNotContain(splitBookBack);
        assertThat(splitBookBack.getUser()).isNull();
    }

    @Test
    void noteTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Note noteBack = getNoteRandomSampleGenerator();

        user.addNote(noteBack);
        assertThat(user.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getUser()).isEqualTo(user);

        user.removeNote(noteBack);
        assertThat(user.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getUser()).isNull();

        user.notes(new HashSet<>(Set.of(noteBack)));
        assertThat(user.getNotes()).containsOnly(noteBack);
        assertThat(noteBack.getUser()).isEqualTo(user);

        user.setNotes(new HashSet<>());
        assertThat(user.getNotes()).doesNotContain(noteBack);
        assertThat(noteBack.getUser()).isNull();
    }

    @Test
    void ledgerTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        user.addLedger(ledgerBack);
        assertThat(user.getLedgers()).containsOnly(ledgerBack);
        assertThat(ledgerBack.getUser()).isEqualTo(user);

        user.removeLedger(ledgerBack);
        assertThat(user.getLedgers()).doesNotContain(ledgerBack);
        assertThat(ledgerBack.getUser()).isNull();

        user.ledgers(new HashSet<>(Set.of(ledgerBack)));
        assertThat(user.getLedgers()).containsOnly(ledgerBack);
        assertThat(ledgerBack.getUser()).isEqualTo(user);

        user.setLedgers(new HashSet<>());
        assertThat(user.getLedgers()).doesNotContain(ledgerBack);
        assertThat(ledgerBack.getUser()).isNull();
    }
}
