package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * A DTO for the Home.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class HomeDTO extends AbstractBaseDTO implements Serializable {

    private LedgerDTO ledger;

    private List<LedgerDTO> ledgers;

    Page<AssetBalanceDTO> assetBalances;

    Page<ObjectivePayBookDTO> objectivePayBooks;

    Page<PaymentMethodDTO> paymentMethods;

    Page<TransactionDTO> transactions;

    List<LocalDate> filterTransactionDates;

    List<SummaryPaymentCategoryDTO> summaryPaymentCategories = new ArrayList<>();

    List<PaymentCategoryDTO> existingPaymentCategories = new ArrayList<>();

    List<PaymentMethodDTO> existingPaymentMethods = new ArrayList<>();
}
