package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.produck.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class TransactionDTO extends AbstractBaseDTO implements Serializable {

    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDate transactionDate;

    private TransactionType transactionType;

    private ObjectiveDTO objective;

    private PaymentMethodDTO paymentMethod;

    private PaymentCategoryDTO paymentCategory;

    private LedgerDTO ledger;
}
