package com.produck.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplitBookDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBookDetailDTO.class);
        SplitBookDetailDTO splitBookDetailDTO1 = new SplitBookDetailDTO();
        splitBookDetailDTO1.setId(1L);
        SplitBookDetailDTO splitBookDetailDTO2 = new SplitBookDetailDTO();
        assertThat(splitBookDetailDTO1).isNotEqualTo(splitBookDetailDTO2);
        splitBookDetailDTO2.setId(splitBookDetailDTO1.getId());
        assertThat(splitBookDetailDTO1).isEqualTo(splitBookDetailDTO2);
        splitBookDetailDTO2.setId(2L);
        assertThat(splitBookDetailDTO1).isNotEqualTo(splitBookDetailDTO2);
        splitBookDetailDTO1.setId(null);
        assertThat(splitBookDetailDTO1).isNotEqualTo(splitBookDetailDTO2);
    }
}
