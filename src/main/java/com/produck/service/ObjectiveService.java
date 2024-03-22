package com.produck.service;

import com.produck.domain.Ledger;
import com.produck.domain.Objective;
import com.produck.domain.PaymentCategory;
import com.produck.domain.Transaction;
import com.produck.domain.enumeration.ObjectiveType;
import com.produck.service.dto.ObjectiveDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.Objective}.
 */
public interface ObjectiveService {
    /**
     * Save a objective.
     *
     * @param objectiveDTO the entity to save.
     * @return the persisted entity.
     */
    ObjectiveDTO save(ObjectiveDTO objectiveDTO);

    /**
     * Updates a objective.
     *
     * @param objectiveDTO the entity to update.
     * @return the persisted entity.
     */
    ObjectiveDTO update(ObjectiveDTO objectiveDTO);

    /**
     * Partially updates a objective.
     *
     * @param objectiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ObjectiveDTO> partialUpdate(ObjectiveDTO objectiveDTO);

    /**
     * Get all the objectives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ObjectiveDTO> findAll(Pageable pageable);

    /**
     * Get the "id" objective.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ObjectiveDTO> findOne(Long id);

    /**
     * Delete the "id" objective.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteBy(Ledger ledger, Long id, ObjectiveType type);

    Objective createBy(Ledger ledger, PaymentCategory paymentCategory, String name, ObjectiveType type);

    Objective createIfNew(Transaction transaction, Objective objective);

    List<ObjectiveDTO> findAllBy(Ledger ledger, PaymentCategory paymentCategory, ObjectiveType type);

    Optional<ObjectiveDTO> findDefaultObjective(Ledger ledger);
}
