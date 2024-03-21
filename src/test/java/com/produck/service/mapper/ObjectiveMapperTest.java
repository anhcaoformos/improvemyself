package com.produck.service.mapper;

import static com.produck.domain.ObjectiveAsserts.*;
import static com.produck.domain.ObjectiveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectiveMapperTest {

    private ObjectiveMapper objectiveMapper;

    @BeforeEach
    void setUp() {
        objectiveMapper = new ObjectiveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getObjectiveSample1();
        var actual = objectiveMapper.toEntity(objectiveMapper.toDto(expected));
        assertObjectiveAllPropertiesEquals(expected, actual);
    }
}
