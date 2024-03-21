package com.produck.domain;

import static com.produck.domain.ApplicationUserTestSamples.*;
import static com.produck.domain.SplitBookDetailTestSamples.*;
import static com.produck.domain.SplitBookJoinerTestSamples.*;
import static com.produck.domain.SplitBookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SplitBookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBook.class);
        SplitBook splitBook1 = getSplitBookSample1();
        SplitBook splitBook2 = new SplitBook();
        assertThat(splitBook1).isNotEqualTo(splitBook2);

        splitBook2.setId(splitBook1.getId());
        assertThat(splitBook1).isEqualTo(splitBook2);

        splitBook2 = getSplitBookSample2();
        assertThat(splitBook1).isNotEqualTo(splitBook2);
    }

    @Test
    void splitBookDetailTest() throws Exception {
        SplitBook splitBook = getSplitBookRandomSampleGenerator();
        SplitBookDetail splitBookDetailBack = getSplitBookDetailRandomSampleGenerator();

        splitBook.addSplitBookDetail(splitBookDetailBack);
        assertThat(splitBook.getSplitBookDetails()).containsOnly(splitBookDetailBack);
        assertThat(splitBookDetailBack.getSplitBook()).isEqualTo(splitBook);

        splitBook.removeSplitBookDetail(splitBookDetailBack);
        assertThat(splitBook.getSplitBookDetails()).doesNotContain(splitBookDetailBack);
        assertThat(splitBookDetailBack.getSplitBook()).isNull();

        splitBook.splitBookDetails(new HashSet<>(Set.of(splitBookDetailBack)));
        assertThat(splitBook.getSplitBookDetails()).containsOnly(splitBookDetailBack);
        assertThat(splitBookDetailBack.getSplitBook()).isEqualTo(splitBook);

        splitBook.setSplitBookDetails(new HashSet<>());
        assertThat(splitBook.getSplitBookDetails()).doesNotContain(splitBookDetailBack);
        assertThat(splitBookDetailBack.getSplitBook()).isNull();
    }

    @Test
    void splitBookJoinerTest() throws Exception {
        SplitBook splitBook = getSplitBookRandomSampleGenerator();
        SplitBookJoiner splitBookJoinerBack = getSplitBookJoinerRandomSampleGenerator();

        splitBook.addSplitBookJoiner(splitBookJoinerBack);
        assertThat(splitBook.getSplitBookJoiners()).containsOnly(splitBookJoinerBack);
        assertThat(splitBookJoinerBack.getSplitBook()).isEqualTo(splitBook);

        splitBook.removeSplitBookJoiner(splitBookJoinerBack);
        assertThat(splitBook.getSplitBookJoiners()).doesNotContain(splitBookJoinerBack);
        assertThat(splitBookJoinerBack.getSplitBook()).isNull();

        splitBook.splitBookJoiners(new HashSet<>(Set.of(splitBookJoinerBack)));
        assertThat(splitBook.getSplitBookJoiners()).containsOnly(splitBookJoinerBack);
        assertThat(splitBookJoinerBack.getSplitBook()).isEqualTo(splitBook);

        splitBook.setSplitBookJoiners(new HashSet<>());
        assertThat(splitBook.getSplitBookJoiners()).doesNotContain(splitBookJoinerBack);
        assertThat(splitBookJoinerBack.getSplitBook()).isNull();
    }

    @Test
    void applicationUserTest() throws Exception {
        SplitBook splitBook = getSplitBookRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        splitBook.setApplicationUser(applicationUserBack);
        assertThat(splitBook.getApplicationUser()).isEqualTo(applicationUserBack);

        splitBook.applicationUser(null);
        assertThat(splitBook.getApplicationUser()).isNull();
    }
}
