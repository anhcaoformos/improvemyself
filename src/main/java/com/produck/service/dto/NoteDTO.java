package com.produck.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.AlertType;
import com.produck.domain.enumeration.NoteType;
import com.produck.domain.enumeration.RepeatType;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.produck.domain.Note} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createdBy", "lastModifiedBy", "lastModifiedDate" })
public class NoteDTO extends AbstractBaseDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private LocalDate noteDateFrom;

    private LocalDate noteDateTo;

    private NoteType noteType;

    private RepeatType repeatType;

    private AlertType alertType;

    private UserDTO user;
}
