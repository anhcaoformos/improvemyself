package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.AlertType;
import com.produck.domain.enumeration.NoteType;
import com.produck.domain.enumeration.RepeatType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "note_date_from")
    private LocalDate noteDateFrom;

    @Column(name = "note_date_to")
    private LocalDate noteDateTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type")
    private NoteType noteType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type")
    private RepeatType repeatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type")
    private AlertType alertType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBooks", "notes", "ledgers" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Note title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Note description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getNoteDateFrom() {
        return this.noteDateFrom;
    }

    public Note noteDateFrom(LocalDate noteDateFrom) {
        this.setNoteDateFrom(noteDateFrom);
        return this;
    }

    public void setNoteDateFrom(LocalDate noteDateFrom) {
        this.noteDateFrom = noteDateFrom;
    }

    public LocalDate getNoteDateTo() {
        return this.noteDateTo;
    }

    public Note noteDateTo(LocalDate noteDateTo) {
        this.setNoteDateTo(noteDateTo);
        return this;
    }

    public void setNoteDateTo(LocalDate noteDateTo) {
        this.noteDateTo = noteDateTo;
    }

    public NoteType getNoteType() {
        return this.noteType;
    }

    public Note noteType(NoteType noteType) {
        this.setNoteType(noteType);
        return this;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public RepeatType getRepeatType() {
        return this.repeatType;
    }

    public Note repeatType(RepeatType repeatType) {
        this.setRepeatType(repeatType);
        return this;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public AlertType getAlertType() {
        return this.alertType;
    }

    public Note alertType(AlertType alertType) {
        this.setAlertType(alertType);
        return this;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Note applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return getId() != null && getId().equals(((Note) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", noteDateFrom='" + getNoteDateFrom() + "'" +
            ", noteDateTo='" + getNoteDateTo() + "'" +
            ", noteType='" + getNoteType() + "'" +
            ", repeatType='" + getRepeatType() + "'" +
            ", alertType='" + getAlertType() + "'" +
            "}";
    }
}
