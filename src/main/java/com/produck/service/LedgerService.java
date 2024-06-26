package com.produck.service;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.service.dto.LedgerDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.Ledger}.
 */
public interface LedgerService {
    /**
     * Save a ledger.
     *
     * @param ledgerDTO the entity to save.
     * @return the persisted entity.
     */
    LedgerDTO save(LedgerDTO ledgerDTO);

    LedgerDTO save(User user, LedgerDTO ledgerDTO);
    /**
     * Updates a ledger.
     *
     * @param ledgerDTO the entity to update.
     * @return the persisted entity.
     */
    LedgerDTO update(LedgerDTO ledgerDTO);

    /**
     * Partially updates a ledger.
     *
     * @param ledgerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LedgerDTO> partialUpdate(LedgerDTO ledgerDTO);

    /**
     * Get all the ledgers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LedgerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ledger.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LedgerDTO> findOne(Long id);

    Optional<LedgerDTO> findDefaultByUser(User user);

    /**
     * Delete the "id" ledger.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<LedgerDTO> findAllByUser(User user, Pageable pageable);

    Optional<LedgerDTO> findOneByUser(User user, Long id);

    void deleteByUser(User user, Long id);

    void setDefault(User user, Long id);

    Ledger createLedgerData(User user, String ledgerName);

    List<LedgerDTO> findAllByUser(User user);
}
