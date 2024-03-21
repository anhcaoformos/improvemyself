package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SplitBookJoiner.
 */
@Entity
@Table(name = "split_book_joiner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookJoiner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBookDetails", "splitBookJoiners", "applicationUser" }, allowSetters = true)
    private SplitBook splitBook;

    @JsonIgnoreProperties(value = { "splitBookJoiner", "splitBook" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "splitBookJoiner")
    private SplitBookDetail splitBookDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SplitBookJoiner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SplitBookJoiner name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SplitBook getSplitBook() {
        return this.splitBook;
    }

    public void setSplitBook(SplitBook splitBook) {
        this.splitBook = splitBook;
    }

    public SplitBookJoiner splitBook(SplitBook splitBook) {
        this.setSplitBook(splitBook);
        return this;
    }

    public SplitBookDetail getSplitBookDetail() {
        return this.splitBookDetail;
    }

    public void setSplitBookDetail(SplitBookDetail splitBookDetail) {
        if (this.splitBookDetail != null) {
            this.splitBookDetail.setSplitBookJoiner(null);
        }
        if (splitBookDetail != null) {
            splitBookDetail.setSplitBookJoiner(this);
        }
        this.splitBookDetail = splitBookDetail;
    }

    public SplitBookJoiner splitBookDetail(SplitBookDetail splitBookDetail) {
        this.setSplitBookDetail(splitBookDetail);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplitBookJoiner)) {
            return false;
        }
        return getId() != null && getId().equals(((SplitBookJoiner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBookJoiner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
