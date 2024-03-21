package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentCategory.
 */
@Entity
@Table(name = "payment_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_hidden")
    private Boolean isHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "goals", "objectives", "transactions", "paymentMethods", "paymentCategories", "applicationUser" },
        allowSetters = true
    )
    private Ledger ledger;

    @JsonIgnoreProperties(value = { "objective", "paymentMethod", "paymentCategory", "ledger" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paymentCategory")
    private Transaction transaction;

    @JsonIgnoreProperties(value = { "paymentCategory", "ledger", "transaction" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paymentCategory")
    private Objective objective;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public PaymentCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public PaymentCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public PaymentCategory isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public PaymentCategory isHidden(Boolean isHidden) {
        this.setIsHidden(isHidden);
        return this;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Ledger getLedger() {
        return this.ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public PaymentCategory ledger(Ledger ledger) {
        this.setLedger(ledger);
        return this;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        if (this.transaction != null) {
            this.transaction.setPaymentCategory(null);
        }
        if (transaction != null) {
            transaction.setPaymentCategory(this);
        }
        this.transaction = transaction;
    }

    public PaymentCategory transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public void setObjective(Objective objective) {
        if (this.objective != null) {
            this.objective.setPaymentCategory(null);
        }
        if (objective != null) {
            objective.setPaymentCategory(this);
        }
        this.objective = objective;
    }

    public PaymentCategory objective(Objective objective) {
        this.setObjective(objective);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCategory{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            "}";
    }
}
