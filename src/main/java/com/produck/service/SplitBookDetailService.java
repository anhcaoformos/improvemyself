package com.produck.service;

import com.produck.service.dto.SplitBookDetailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.SplitBookDetail}.
 */
public interface SplitBookDetailService {
    /**
     * Save a splitBookDetail.
     *
     * @param splitBookDetailDTO the entity to save.
     * @return the persisted entity.
     */
    SplitBookDetailDTO save(SplitBookDetailDTO splitBookDetailDTO);

    /**
     * Updates a splitBookDetail.
     *
     * @param splitBookDetailDTO the entity to update.
     * @return the persisted entity.
     */
    SplitBookDetailDTO update(SplitBookDetailDTO splitBookDetailDTO);

    /**
     * Partially updates a splitBookDetail.
     *
     * @param splitBookDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SplitBookDetailDTO> partialUpdate(SplitBookDetailDTO splitBookDetailDTO);

    /**
     * Get all the splitBookDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplitBookDetailDTO> findAll(Pageable pageable);

    /**
     * Get the "id" splitBookDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SplitBookDetailDTO> findOne(Long id);

    /**
     * Delete the "id" splitBookDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
