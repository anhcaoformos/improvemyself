package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ledger.
 */
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
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ledger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ledger name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public Ledger isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Set<Goal> getGoals() {
        return this.goals;
    }

    public void setGoals(Set<Goal> goals) {
        if (this.goals != null) {
            this.goals.forEach(i -> i.setLedger(null));
        }
        if (goals != null) {
            goals.forEach(i -> i.setLedger(this));
        }
        this.goals = goals;
    }

    public Ledger goals(Set<Goal> goals) {
        this.setGoals(goals);
        return this;
    }

    public Ledger addGoal(Goal goal) {
        this.goals.add(goal);
        goal.setLedger(this);
        return this;
    }

    public Ledger removeGoal(Goal goal) {
        this.goals.remove(goal);
        goal.setLedger(null);
        return this;
    }

    public Set<Objective> getObjectives() {
        return this.objectives;
    }

    public void setObjectives(Set<Objective> objectives) {
        if (this.objectives != null) {
            this.objectives.forEach(i -> i.setLedger(null));
        }
        if (objectives != null) {
            objectives.forEach(i -> i.setLedger(this));
        }
        this.objectives = objectives;
    }

    public Ledger objectives(Set<Objective> objectives) {
        this.setObjectives(objectives);
        return this;
    }

    public Ledger addObjective(Objective objective) {
        this.objectives.add(objective);
        objective.setLedger(this);
        return this;
    }

    public Ledger removeObjective(Objective objective) {
        this.objectives.remove(objective);
        objective.setLedger(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setLedger(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setLedger(this));
        }
        this.transactions = transactions;
    }

    public Ledger transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Ledger addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setLedger(this);
        return this;
    }

    public Ledger removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setLedger(null);
        return this;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        if (this.paymentMethods != null) {
            this.paymentMethods.forEach(i -> i.setLedger(null));
        }
        if (paymentMethods != null) {
            paymentMethods.forEach(i -> i.setLedger(this));
        }
        this.paymentMethods = paymentMethods;
    }

    public Ledger paymentMethods(Set<PaymentMethod> paymentMethods) {
        this.setPaymentMethods(paymentMethods);
        return this;
    }

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

    public Set<PaymentCategory> getPaymentCategories() {
        return this.paymentCategories;
    }

    public void setPaymentCategories(Set<PaymentCategory> paymentCategories) {
        if (this.paymentCategories != null) {
            this.paymentCategories.forEach(i -> i.setLedger(null));
        }
        if (paymentCategories != null) {
            paymentCategories.forEach(i -> i.setLedger(this));
        }
        this.paymentCategories = paymentCategories;
    }

    public Ledger paymentCategories(Set<PaymentCategory> paymentCategories) {
        this.setPaymentCategories(paymentCategories);
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

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Ledger applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ledger)) {
            return false;
        }
        return getId() != null && getId().equals(((Ledger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ledger{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            "}";
    }
}
