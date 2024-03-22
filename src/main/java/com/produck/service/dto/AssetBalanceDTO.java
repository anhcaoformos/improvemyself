package com.produck.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("common-java:DuplicatedBlocks")
public interface AssetBalanceDTO {
    String getId();

    BigDecimal getTotalAmount();

    LocalDate getTransactionDate();

    Long getLedgerId();

    String getPaymentMethodName();
}
