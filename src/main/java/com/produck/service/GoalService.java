package com.produck.service;

import com.produck.service.dto.GoalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.Goal}.
 */
public interface GoalService {
    /**
     * Save a goal.
     *
     * @param goalDTO the entity to save.
     * @return the persisted entity.
     */
    GoalDTO save(GoalDTO goalDTO);

    /**
     * Updates a goal.
     *
     * @param goalDTO the entity to update.
     * @return the persisted entity.
     */
    GoalDTO update(GoalDTO goalDTO);

    /**
     * Partially updates a goal.
     *
     * @param goalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GoalDTO> partialUpdate(GoalDTO goalDTO);

    /**
     * Get all the goals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GoalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" goal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GoalDTO> findOne(Long id);

    /**
     * Delete the "id" goal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
