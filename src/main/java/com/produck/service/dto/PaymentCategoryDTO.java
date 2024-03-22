package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.produck.domain.PaymentCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class PaymentCategoryDTO extends AbstractBaseDTO implements Serializable {

    private Long id;

    private String code;

    private String name;

    private Boolean isDefault;

    private Boolean isHidden;

    private LedgerDTO ledger;
}
