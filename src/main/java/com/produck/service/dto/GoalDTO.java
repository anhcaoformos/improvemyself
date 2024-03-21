package com.produck.service.dto;

import com.produck.domain.enumeration.Priority;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.produck.domain.Goal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GoalDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private Priority priority;

    private LedgerDTO ledger;

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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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
        if (!(o instanceof GoalDTO)) {
            return false;
        }

        GoalDTO goalDTO = (GoalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, goalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GoalDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", ledger=" + getLedger() +
            "}";
    }
}
