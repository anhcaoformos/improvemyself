package com.produck.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.SplitBook} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookDTO implements Serializable {

    private Long id;

    private String description;

    private String name;

    private ApplicationUserDTO applicationUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationUserDTO getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUserDTO applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplitBookDTO)) {
            return false;
        }

        SplitBookDTO splitBookDTO = (SplitBookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, splitBookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplitBookDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", name='" + getName() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
