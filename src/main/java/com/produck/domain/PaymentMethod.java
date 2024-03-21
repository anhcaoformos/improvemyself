package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.PaymentMethodType;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "payment_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PaymentMethodType type;

    @Column(name = "is_hidden")
    private Boolean isHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "goals", "objectives", "transactions", "paymentMethods", "paymentCategories", "applicationUser" },
        allowSetters = true
    )
    private Ledger ledger;

    @JsonIgnoreProperties(value = { "objective", "paymentMethod", "paymentCategory", "ledger" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paymentMethod")
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public PaymentMethod code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public PaymentMethod name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PaymentMethodType getType() {
        return this.type;
    }

    public PaymentMethod type(PaymentMethodType type) {
        this.setType(type);
        return this;
    }

    public void setType(PaymentMethodType type) {
        this.type = type;
    }

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public PaymentMethod isHidden(Boolean isHidden) {
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

    public PaymentMethod ledger(Ledger ledger) {
        this.setLedger(ledger);
        return this;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        if (this.transaction != null) {
            this.transaction.setPaymentMethod(null);
        }
        if (transaction != null) {
            transaction.setPaymentMethod(this);
        }
        this.transaction = transaction;
    }

    public PaymentMethod transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentMethod) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            "}";
    }
}
