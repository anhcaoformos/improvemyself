package com.produck.service.mapper;

import static com.produck.domain.SplitBookDetailAsserts.*;
import static com.produck.domain.SplitBookDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplitBookDetailMapperTest {

    private SplitBookDetailMapper splitBookDetailMapper;

    @BeforeEach
    void setUp() {
        splitBookDetailMapper = new SplitBookDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSplitBookDetailSample1();
        var actual = splitBookDetailMapper.toEntity(splitBookDetailMapper.toDto(expected));
        assertSplitBookDetailAllPropertiesEquals(expected, actual);
    }
}
