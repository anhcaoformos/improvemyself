package com.produck.domain;

import static com.produck.domain.SplitBookDetailTestSamples.*;
import static com.produck.domain.SplitBookJoinerTestSamples.*;
import static com.produck.domain.SplitBookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplitBookJoinerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBookJoiner.class);
        SplitBookJoiner splitBookJoiner1 = getSplitBookJoinerSample1();
        SplitBookJoiner splitBookJoiner2 = new SplitBookJoiner();
        assertThat(splitBookJoiner1).isNotEqualTo(splitBookJoiner2);

        splitBookJoiner2.setId(splitBookJoiner1.getId());
        assertThat(splitBookJoiner1).isEqualTo(splitBookJoiner2);

        splitBookJoiner2 = getSplitBookJoinerSample2();
        assertThat(splitBookJoiner1).isNotEqualTo(splitBookJoiner2);
    }

    @Test
    void splitBookTest() throws Exception {
        SplitBookJoiner splitBookJoiner = getSplitBookJoinerRandomSampleGenerator();
        SplitBook splitBookBack = getSplitBookRandomSampleGenerator();

        splitBookJoiner.setSplitBook(splitBookBack);
        assertThat(splitBookJoiner.getSplitBook()).isEqualTo(splitBookBack);

        splitBookJoiner.splitBook(null);
        assertThat(splitBookJoiner.getSplitBook()).isNull();
    }

    @Test
    void splitBookDetailTest() throws Exception {
        SplitBookJoiner splitBookJoiner = getSplitBookJoinerRandomSampleGenerator();
        SplitBookDetail splitBookDetailBack = getSplitBookDetailRandomSampleGenerator();

        splitBookJoiner.setSplitBookDetail(splitBookDetailBack);
        assertThat(splitBookJoiner.getSplitBookDetail()).isEqualTo(splitBookDetailBack);
        assertThat(splitBookDetailBack.getSplitBookJoiner()).isEqualTo(splitBookJoiner);

        splitBookJoiner.splitBookDetail(null);
        assertThat(splitBookJoiner.getSplitBookDetail()).isNull();
        assertThat(splitBookDetailBack.getSplitBookJoiner()).isNull();
    }
}
