package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.PaymentMethodType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.produck.domain.PaymentMethod} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class PaymentMethodDTO extends AbstractBaseDTO implements Serializable {

    private Long id;

    private String code;

    private String name;

    private PaymentMethodType type;

    private Boolean isHidden;

    private LedgerDTO ledger;
}
