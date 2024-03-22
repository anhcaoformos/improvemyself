package com.produck.service;

import com.produck.domain.User;
import com.produck.service.dto.SplitBookDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.SplitBook}.
 */
public interface SplitBookService {
    /**
     * Save a splitBook.
     *
     * @param splitBookDTO the entity to save.
     * @return the persisted entity.
     */
    SplitBookDTO save(SplitBookDTO splitBookDTO);

    /**
     * Updates a splitBook.
     *
     * @param splitBookDTO the entity to update.
     * @return the persisted entity.
     */
    SplitBookDTO update(SplitBookDTO splitBookDTO);

    /**
     * Partially updates a splitBook.
     *
     * @param splitBookDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SplitBookDTO> partialUpdate(SplitBookDTO splitBookDTO);

    /**
     * Get all the splitBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplitBookDTO> findAll(Pageable pageable);

    /**
     * Get the "id" splitBook.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SplitBookDTO> findOne(Long id);

    /**
     * Delete the "id" splitBook.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<SplitBookDTO> findAllByUser(User user, Pageable pageable);

    Optional<SplitBookDTO> findOneByUser(User user, Long id);

    void deleteByUser(User user, Long id);

    SplitBookDTO save(User user, SplitBookDTO splitBookDTO);
}
