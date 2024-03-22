package com.produck.service;

import com.produck.service.dto.SplitBookJoinerDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.SplitBookJoiner}.
 */
public interface SplitBookJoinerService {
    /**
     * Save a splitBookJoiner.
     *
     * @param splitBookJoinerDTO the entity to save.
     * @return the persisted entity.
     */
    SplitBookJoinerDTO save(SplitBookJoinerDTO splitBookJoinerDTO);

    /**
     * Updates a splitBookJoiner.
     *
     * @param splitBookJoinerDTO the entity to update.
     * @return the persisted entity.
     */
    SplitBookJoinerDTO update(SplitBookJoinerDTO splitBookJoinerDTO);

    /**
     * Partially updates a splitBookJoiner.
     *
     * @param splitBookJoinerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SplitBookJoinerDTO> partialUpdate(SplitBookJoinerDTO splitBookJoinerDTO);

    /**
     * Get all the splitBookJoiners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplitBookJoinerDTO> findAll(Pageable pageable);

    //    /**
    //     * Get all the SplitBookJoinerDTO where SplitBookDetail is {@code null}.
    //     *
    //     * @return the {@link List} of entities.
    //     */
    //    List<SplitBookJoinerDTO> findAllWhereSplitBookDetailIsNull();

    /**
     * Get the "id" splitBookJoiner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SplitBookJoinerDTO> findOne(Long id);

    /**
     * Delete the "id" splitBookJoiner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
