package com.produck.service.dto;

import java.math.BigDecimal;

@SuppressWarnings("common-java:DuplicatedBlocks")
public interface SummaryPaymentCategoryDTO {
    Long getLedgerId();

    String getName();

    String getType();

    BigDecimal getTotalAmount();
}
