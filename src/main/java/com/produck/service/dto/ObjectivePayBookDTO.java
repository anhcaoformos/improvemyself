package com.produck.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.andee.domain.PayBook} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public interface ObjectivePayBookDTO {
    LocalDate getTransactionDate();

    String getObjectiveName();

    BigDecimal getAmount();

    Long getLedgerId();
}
