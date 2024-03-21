package com.produck.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplitBookDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplitBookDTO.class);
        SplitBookDTO splitBookDTO1 = new SplitBookDTO();
        splitBookDTO1.setId(1L);
        SplitBookDTO splitBookDTO2 = new SplitBookDTO();
        assertThat(splitBookDTO1).isNotEqualTo(splitBookDTO2);
        splitBookDTO2.setId(splitBookDTO1.getId());
        assertThat(splitBookDTO1).isEqualTo(splitBookDTO2);
        splitBookDTO2.setId(2L);
        assertThat(splitBookDTO1).isNotEqualTo(splitBookDTO2);
        splitBookDTO1.setId(null);
        assertThat(splitBookDTO1).isNotEqualTo(splitBookDTO2);
    }
}
