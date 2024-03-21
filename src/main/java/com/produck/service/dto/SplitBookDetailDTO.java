package com.produck.service.dto;

import com.produck.domain.enumeration.JoinerRole;
import com.produck.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.SplitBookDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookDetailDTO implements Serializable {

    private Long id;

    private Double amount;

    private String description;

    private String personName;

    private LocalDate transactionDate;

    private TransactionType transactionType;

    private String groupId;

    private JoinerRole joinerRole;

    private SplitBookJoinerDTO splitBookJoiner;

    private SplitBookDTO splitBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public JoinerRole getJoinerRole() {
        return joinerRole;
    }

    public void setJoinerRole(JoinerRole joinerRole) {
        this.joinerRole = joinerRole;
    }

    public SplitBookJoinerDTO getSplitBookJoiner() {
        return splitBookJoiner;
    }

    public void setSplitBookJoiner(SplitBookJoinerDTO splitBookJoiner) {
        this.splitBookJoiner = splitBookJoiner;
    }

    public SplitBookDTO getSplitBook() {
        return splitBook;
    }

    public void setSplitBook(SplitBookDTO splitBook) {
        this.splitBook = splitBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplitBookDetailDTO)) {
            return false;
        }

        SplitBookDetailDTO splitBookDetailDTO = (SplitBookDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, splitBookDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBookDetailDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", personName='" + getPersonName() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", joinerRole='" + getJoinerRole() + "'" +
            ", splitBookJoiner=" + getSplitBookJoiner() +
            ", splitBook=" + getSplitBook() +
            "}";
    }
}
