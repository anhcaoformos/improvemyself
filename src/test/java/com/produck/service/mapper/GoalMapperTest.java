package com.produck.service.mapper;

import static com.produck.domain.GoalAsserts.*;
import static com.produck.domain.GoalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoalMapperTest {

    private GoalMapper goalMapper;

    @BeforeEach
    void setUp() {
        goalMapper = new GoalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGoalSample1();
        var actual = goalMapper.toEntity(goalMapper.toDto(expected));
        assertGoalAllPropertiesEquals(expected, actual);
    }
}
