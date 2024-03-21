package com.produck.domain;

import static com.produck.domain.SplitBookDetailTestSamples.*;
import static com.produck.domain.SplitBookJoinerTestSamples.*;
import static com.produck.domain.SplitBookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplitBookDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBookDetail.class);
        SplitBookDetail splitBookDetail1 = getSplitBookDetailSample1();
        SplitBookDetail splitBookDetail2 = new SplitBookDetail();
        assertThat(splitBookDetail1).isNotEqualTo(splitBookDetail2);

        splitBookDetail2.setId(splitBookDetail1.getId());
        assertThat(splitBookDetail1).isEqualTo(splitBookDetail2);

        splitBookDetail2 = getSplitBookDetailSample2();
        assertThat(splitBookDetail1).isNotEqualTo(splitBookDetail2);
    }

    @Test
    void splitBookJoinerTest() throws Exception {
        SplitBookDetail splitBookDetail = getSplitBookDetailRandomSampleGenerator();
        SplitBookJoiner splitBookJoinerBack = getSplitBookJoinerRandomSampleGenerator();

        splitBookDetail.setSplitBookJoiner(splitBookJoinerBack);
        assertThat(splitBookDetail.getSplitBookJoiner()).isEqualTo(splitBookJoinerBack);

        splitBookDetail.splitBookJoiner(null);
        assertThat(splitBookDetail.getSplitBookJoiner()).isNull();
    }

    @Test
    void splitBookTest() throws Exception {
        SplitBookDetail splitBookDetail = getSplitBookDetailRandomSampleGenerator();
        SplitBook splitBookBack = getSplitBookRandomSampleGenerator();

        splitBookDetail.setSplitBook(splitBookBack);
        assertThat(splitBookDetail.getSplitBook()).isEqualTo(splitBookBack);

        splitBookDetail.splitBook(null);
        assertThat(splitBookDetail.getSplitBook()).isNull();
    }
}
