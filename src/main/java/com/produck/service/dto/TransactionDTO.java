package com.produck.service.dto;

import com.produck.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDate transactionDate;

    private TransactionType transactionType;

    private ObjectiveDTO objective;

    private PaymentMethodDTO paymentMethod;

    private PaymentCategoryDTO paymentCategory;

    private LedgerDTO ledger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public ObjectiveDTO getObjective() {
        return objective;
    }

    public void setObjective(ObjectiveDTO objective) {
        this.objective = objective;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public LedgerDTO getLedger() {
        return ledger;
    }

    public void setLedger(LedgerDTO ledger) {
        this.ledger = ledger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", objective=" + getObjective() +
            ", paymentMethod=" + getPaymentMethod() +
            ", paymentCategory=" + getPaymentCategory() +
            ", ledger=" + getLedger() +
            "}";
    }
}
