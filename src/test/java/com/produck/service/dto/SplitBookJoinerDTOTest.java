package com.produck.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplitBookJoinerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBookJoinerDTO.class);
        SplitBookJoinerDTO splitBookJoinerDTO1 = new SplitBookJoinerDTO();
        splitBookJoinerDTO1.setId(1L);
        SplitBookJoinerDTO splitBookJoinerDTO2 = new SplitBookJoinerDTO();
        assertThat(splitBookJoinerDTO1).isNotEqualTo(splitBookJoinerDTO2);
        splitBookJoinerDTO2.setId(splitBookJoinerDTO1.getId());
        assertThat(splitBookJoinerDTO1).isEqualTo(splitBookJoinerDTO2);
        splitBookJoinerDTO2.setId(2L);
        assertThat(splitBookJoinerDTO1).isNotEqualTo(splitBookJoinerDTO2);
        splitBookJoinerDTO1.setId(null);
        assertThat(splitBookJoinerDTO1).isNotEqualTo(splitBookJoinerDTO2);
    }
}
