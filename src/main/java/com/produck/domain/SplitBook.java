package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SplitBook.
 */
@Entity
@Table(name = "split_book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "splitBook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "splitBookJoiner", "splitBook" }, allowSetters = true)
    private Set<SplitBookDetail> splitBookDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "splitBook")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "splitBook", "splitBookDetail" }, allowSetters = true)
    private Set<SplitBookJoiner> splitBookJoiners = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBooks", "notes", "ledgers" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SplitBook id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public SplitBook description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public SplitBook name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SplitBookDetail> getSplitBookDetails() {
        return this.splitBookDetails;
    }

    public void setSplitBookDetails(Set<SplitBookDetail> splitBookDetails) {
        if (this.splitBookDetails != null) {
            this.splitBookDetails.forEach(i -> i.setSplitBook(null));
        }
        if (splitBookDetails != null) {
            splitBookDetails.forEach(i -> i.setSplitBook(this));
        }
        this.splitBookDetails = splitBookDetails;
    }

    public SplitBook splitBookDetails(Set<SplitBookDetail> splitBookDetails) {
        this.setSplitBookDetails(splitBookDetails);
        return this;
    }

    public SplitBook addSplitBookDetail(SplitBookDetail splitBookDetail) {
        this.splitBookDetails.add(splitBookDetail);
        splitBookDetail.setSplitBook(this);
        return this;
    }

    public SplitBook removeSplitBookDetail(SplitBookDetail splitBookDetail) {
        this.splitBookDetails.remove(splitBookDetail);
        splitBookDetail.setSplitBook(null);
        return this;
    }

    public Set<SplitBookJoiner> getSplitBookJoiners() {
        return this.splitBookJoiners;
    }

    public void setSplitBookJoiners(Set<SplitBookJoiner> splitBookJoiners) {
        if (this.splitBookJoiners != null) {
            this.splitBookJoiners.forEach(i -> i.setSplitBook(null));
        }
        if (splitBookJoiners != null) {
            splitBookJoiners.forEach(i -> i.setSplitBook(this));
        }
        this.splitBookJoiners = splitBookJoiners;
    }

    public SplitBook splitBookJoiners(Set<SplitBookJoiner> splitBookJoiners) {
        this.setSplitBookJoiners(splitBookJoiners);
        return this;
    }

    public SplitBook addSplitBookJoiner(SplitBookJoiner splitBookJoiner) {
        this.splitBookJoiners.add(splitBookJoiner);
        splitBookJoiner.setSplitBook(this);
        return this;
    }

    public SplitBook removeSplitBookJoiner(SplitBookJoiner splitBookJoiner) {
        this.splitBookJoiners.remove(splitBookJoiner);
        splitBookJoiner.setSplitBook(null);
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public SplitBook applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplitBook)) {
            return false;
        }
        return getId() != null && getId().equals(((SplitBook) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBook{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
