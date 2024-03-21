package com.produck.service.mapper;

import static com.produck.domain.PaymentCategoryAsserts.*;
import static com.produck.domain.PaymentCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentCategoryMapperTest {

    private PaymentCategoryMapper paymentCategoryMapper;

    @BeforeEach
    void setUp() {
        paymentCategoryMapper = new PaymentCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentCategorySample1();
        var actual = paymentCategoryMapper.toEntity(paymentCategoryMapper.toDto(expected));
        assertPaymentCategoryAllPropertiesEquals(expected, actual);
    }
}
