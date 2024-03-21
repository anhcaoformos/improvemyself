package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.JoinerRole;
import com.produck.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SplitBookDetail.
 */
@Entity
@Table(name = "split_book_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "group_id")
    private String groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "joiner_role")
    private JoinerRole joinerRole;

    @JsonIgnoreProperties(value = { "splitBook", "splitBookDetail" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SplitBookJoiner splitBookJoiner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBookDetails", "splitBookJoiners", "applicationUser" }, allowSetters = true)
    private SplitBook splitBook;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SplitBookDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public SplitBookDetail amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public SplitBookDetail description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonName() {
        return this.personName;
    }

    public SplitBookDetail personName(String personName) {
        this.setPersonName(personName);
        return this;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public SplitBookDetail transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public SplitBookDetail transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public SplitBookDetail groupId(String groupId) {
        this.setGroupId(groupId);
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public JoinerRole getJoinerRole() {
        return this.joinerRole;
    }

    public SplitBookDetail joinerRole(JoinerRole joinerRole) {
        this.setJoinerRole(joinerRole);
        return this;
    }

    public void setJoinerRole(JoinerRole joinerRole) {
        this.joinerRole = joinerRole;
    }

    public SplitBookJoiner getSplitBookJoiner() {
        return this.splitBookJoiner;
    }

    public void setSplitBookJoiner(SplitBookJoiner splitBookJoiner) {
        this.splitBookJoiner = splitBookJoiner;
    }

    public SplitBookDetail splitBookJoiner(SplitBookJoiner splitBookJoiner) {
        this.setSplitBookJoiner(splitBookJoiner);
        return this;
    }

    public SplitBook getSplitBook() {
        return this.splitBook;
    }

    public void setSplitBook(SplitBook splitBook) {
        this.splitBook = splitBook;
    }

    public SplitBookDetail splitBook(SplitBook splitBook) {
        this.setSplitBook(splitBook);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplitBookDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((SplitBookDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBookDetail{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", personName='" + getPersonName() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", joinerRole='" + getJoinerRole() + "'" +
            "}";
    }
}
