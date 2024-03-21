package com.produck.service.mapper;

import static com.produck.domain.SplitBookJoinerAsserts.*;
import static com.produck.domain.SplitBookJoinerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplitBookJoinerMapperTest {

    private SplitBookJoinerMapper splitBookJoinerMapper;

    @BeforeEach
    void setUp() {
        splitBookJoinerMapper = new SplitBookJoinerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSplitBookJoinerSample1();
        var actual = splitBookJoinerMapper.toEntity(splitBookJoinerMapper.toDto(expected));
        assertSplitBookJoinerAllPropertiesEquals(expected, actual);
    }
}
