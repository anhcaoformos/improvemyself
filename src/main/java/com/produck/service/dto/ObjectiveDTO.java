package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.ObjectiveType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.produck.domain.Objective} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class ObjectiveDTO extends AbstractBaseDTO implements Serializable {

    private Long id;

    private String name;

    private ObjectiveType type;

    private Boolean isHidden;

    private PaymentCategoryDTO paymentCategory;

    private LedgerDTO ledger;
}
