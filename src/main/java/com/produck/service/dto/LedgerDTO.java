package com.produck.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.Ledger} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LedgerDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean isDefault;

    private ApplicationUserDTO applicationUser;

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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
        if (!(o instanceof LedgerDTO)) {
            return false;
        }

        LedgerDTO ledgerDTO = (LedgerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ledgerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LedgerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
