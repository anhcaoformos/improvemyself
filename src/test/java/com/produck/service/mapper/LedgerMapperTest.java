package com.produck.service.mapper;

import static com.produck.domain.LedgerAsserts.*;
import static com.produck.domain.LedgerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LedgerMapperTest {

    private LedgerMapper ledgerMapper;

    @BeforeEach
    void setUp() {
        ledgerMapper = new LedgerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLedgerSample1();
        var actual = ledgerMapper.toEntity(ledgerMapper.toDto(expected));
        assertLedgerAllPropertiesEquals(expected, actual);
    }
}
