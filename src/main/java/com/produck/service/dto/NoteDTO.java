package com.produck.service.dto;

import com.produck.domain.enumeration.AlertType;
import com.produck.domain.enumeration.NoteType;
import com.produck.domain.enumeration.RepeatType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.Note} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NoteDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private LocalDate noteDateFrom;

    private LocalDate noteDateTo;

    private NoteType noteType;

    private RepeatType repeatType;

    private AlertType alertType;

    private ApplicationUserDTO applicationUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getNoteDateFrom() {
        return noteDateFrom;
    }

    public void setNoteDateFrom(LocalDate noteDateFrom) {
        this.noteDateFrom = noteDateFrom;
    }

    public LocalDate getNoteDateTo() {
        return noteDateTo;
    }

    public void setNoteDateTo(LocalDate noteDateTo) {
        this.noteDateTo = noteDateTo;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
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
        if (!(o instanceof NoteDTO)) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", noteDateFrom='" + getNoteDateFrom() + "'" +
            ", noteDateTo='" + getNoteDateTo() + "'" +
            ", noteType='" + getNoteType() + "'" +
            ", repeatType='" + getRepeatType() + "'" +
            ", alertType='" + getAlertType() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
