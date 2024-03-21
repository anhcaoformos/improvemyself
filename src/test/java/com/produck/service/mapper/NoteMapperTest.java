package com.produck.service.mapper;

import static com.produck.domain.NoteAsserts.*;
import static com.produck.domain.NoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoteMapperTest {

    private NoteMapper noteMapper;

    @BeforeEach
    void setUp() {
        noteMapper = new NoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNoteSample1();
        var actual = noteMapper.toEntity(noteMapper.toDto(expected));
        assertNoteAllPropertiesEquals(expected, actual);
    }
}
