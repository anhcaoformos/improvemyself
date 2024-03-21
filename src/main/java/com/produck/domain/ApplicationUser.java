package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "splitBookDetails", "splitBookJoiners", "applicationUser" }, allowSetters = true)
    private Set<SplitBook> splitBooks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUser" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "goals", "objectives", "transactions", "paymentMethods", "paymentCategories", "applicationUser" },
        allowSetters = true
    )
    private Set<Ledger> ledgers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SplitBook> getSplitBooks() {
        return this.splitBooks;
    }

    public void setSplitBooks(Set<SplitBook> splitBooks) {
        if (this.splitBooks != null) {
            this.splitBooks.forEach(i -> i.setApplicationUser(null));
        }
        if (splitBooks != null) {
            splitBooks.forEach(i -> i.setApplicationUser(this));
        }
        this.splitBooks = splitBooks;
    }

    public ApplicationUser splitBooks(Set<SplitBook> splitBooks) {
        this.setSplitBooks(splitBooks);
        return this;
    }

    public ApplicationUser addSplitBook(SplitBook splitBook) {
        this.splitBooks.add(splitBook);
        splitBook.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeSplitBook(SplitBook splitBook) {
        this.splitBooks.remove(splitBook);
        splitBook.setApplicationUser(null);
        return this;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setApplicationUser(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setApplicationUser(this));
        }
        this.notes = notes;
    }

    public ApplicationUser notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public ApplicationUser addNote(Note note) {
        this.notes.add(note);
        note.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeNote(Note note) {
        this.notes.remove(note);
        note.setApplicationUser(null);
        return this;
    }

    public Set<Ledger> getLedgers() {
        return this.ledgers;
    }

    public void setLedgers(Set<Ledger> ledgers) {
        if (this.ledgers != null) {
            this.ledgers.forEach(i -> i.setApplicationUser(null));
        }
        if (ledgers != null) {
            ledgers.forEach(i -> i.setApplicationUser(this));
        }
        this.ledgers = ledgers;
    }

    public ApplicationUser ledgers(Set<Ledger> ledgers) {
        this.setLedgers(ledgers);
        return this;
    }

    public ApplicationUser addLedger(Ledger ledger) {
        this.ledgers.add(ledger);
        ledger.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeLedger(Ledger ledger) {
        this.ledgers.remove(ledger);
        ledger.setApplicationUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            "}";
    }
}
