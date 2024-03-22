package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ledger.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ledger")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ledger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ledger")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ledger" }, allowSetters = true)
    private Set<Goal> goals = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ledger")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentCategory", "ledger", "transaction" }, allowSetters = true)
    private Set<Objective> objectives = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ledger")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "objective", "paymentMethod", "paymentCategory", "ledger" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ledger")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ledger", "transaction" }, allowSetters = true)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ledger")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ledger", "transaction", "objective" }, allowSetters = true)
    private Set<PaymentCategory> paymentCategories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBooks", "notes", "ledgers" }, allowSetters = true)
    private User user;

    public Ledger addPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
        paymentMethod.setLedger(this);
        return this;
    }

    public Ledger removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
        paymentMethod.setLedger(null);
        return this;
    }

    public Ledger addPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.add(paymentCategory);
        paymentCategory.setLedger(this);
        return this;
    }

    public Ledger removePaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.remove(paymentCategory);
        paymentCategory.setLedger(null);
        return this;
    }
}
