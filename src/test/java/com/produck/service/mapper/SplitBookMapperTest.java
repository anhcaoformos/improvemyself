package com.produck.service.mapper;

import static com.produck.domain.SplitBookAsserts.*;
import static com.produck.domain.SplitBookTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplitBookMapperTest {

    private SplitBookMapper splitBookMapper;

    @BeforeEach
    void setUp() {
        splitBookMapper = new SplitBookMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSplitBookSample1();
        var actual = splitBookMapper.toEntity(splitBookMapper.toDto(expected));
        assertSplitBookAllPropertiesEquals(expected, actual);
    }
}
