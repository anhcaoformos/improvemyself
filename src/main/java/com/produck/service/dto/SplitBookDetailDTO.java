package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.JoinerRole;
import com.produck.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.wildfly.common.annotation.NotNull;

/**
 * A DTO for the {@link com.produck.domain.SplitBookDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class SplitBookDetailDTO extends AbstractBaseDTO implements Serializable {

    @NotNull
    private String sharedKey;

    private Long id;

    private Double amount;

    private String description;

    private String personName;

    private LocalDate transactionDate;

    private TransactionType transactionType;

    private String groupId;

    private JoinerRole joinerRole;

    private SplitBookJoinerDTO splitBookJoiner;

    private SplitBookDTO splitBook;
}
