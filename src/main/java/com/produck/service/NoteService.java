package com.produck.service;

import com.produck.domain.User;
import com.produck.service.dto.NoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param noteDTO the entity to save.
     * @return the persisted entity.
     */
    NoteDTO save(NoteDTO noteDTO);

    /**
     * Updates a note.
     *
     * @param noteDTO the entity to update.
     * @return the persisted entity.
     */
    NoteDTO update(NoteDTO noteDTO);

    /**
     * Partially updates a note.
     *
     * @param noteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NoteDTO> partialUpdate(NoteDTO noteDTO);

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NoteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NoteDTO> findOne(Long id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<NoteDTO> findAllByUser(User user, Pageable pageable);

    Optional<NoteDTO> findOneByUser(User user, Long id);

    void deleteByUser(User user, Long id);
}
