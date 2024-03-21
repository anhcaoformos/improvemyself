package com.produck.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.SplitBookJoiner} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookJoinerDTO implements Serializable {

    private Long id;

    private String name;

    private SplitBookDTO splitBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof SplitBookJoinerDTO)) {
            return false;
        }

        SplitBookJoinerDTO splitBookJoinerDTO = (SplitBookJoinerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, splitBookJoinerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBookJoinerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", splitBook=" + getSplitBook() +
            "}";
    }
}
