package com.produck.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.PaymentCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCategoryDTO implements Serializable {

    private Long id;

    private String code;

    private String name;

    private Boolean isDefault;

    private Boolean isHidden;

    private LedgerDTO ledger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public LedgerDTO getLedger() {
        return ledger;
    }

    public void setLedger(LedgerDTO ledger) {
        this.ledger = ledger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCategoryDTO)) {
            return false;
        }

        PaymentCategoryDTO paymentCategoryDTO = (PaymentCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCategoryDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            ", ledger=" + getLedger() +
            "}";
    }
}
