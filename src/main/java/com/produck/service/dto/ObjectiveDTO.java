package com.produck.service.dto;

import com.produck.domain.enumeration.ObjectiveType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.Objective} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ObjectiveDTO implements Serializable {

    private Long id;

    private String name;

    private ObjectiveType type;

    private Boolean isHidden;

    private PaymentCategoryDTO paymentCategory;

    private LedgerDTO ledger;

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

    public ObjectiveType getType() {
        return type;
    }

    public void setType(ObjectiveType type) {
        this.type = type;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
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
        if (!(o instanceof ObjectiveDTO)) {
            return false;
        }

        ObjectiveDTO objectiveDTO = (ObjectiveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, objectiveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObjectiveDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", isHidden='" + getIsHidden() + "'" +
            ", paymentCategory=" + getPaymentCategory() +
            ", ledger=" + getLedger() +
            "}";
    }
}
