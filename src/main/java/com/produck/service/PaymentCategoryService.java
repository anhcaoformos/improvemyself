package com.produck.service;

import com.produck.domain.Ledger;
import com.produck.service.dto.PaymentCategoryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.PaymentCategory}.
 */
public interface PaymentCategoryService {
    /**
     * Save a paymentCategory.
     *
     * @param paymentCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentCategoryDTO save(PaymentCategoryDTO paymentCategoryDTO);

    /**
     * Updates a paymentCategory.
     *
     * @param paymentCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentCategoryDTO update(PaymentCategoryDTO paymentCategoryDTO);

    /**
     * Partially updates a paymentCategory.
     *
     * @param paymentCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentCategoryDTO> partialUpdate(PaymentCategoryDTO paymentCategoryDTO);

    /**
     * Get all the paymentCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" paymentCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<PaymentCategoryDTO> findAllByLedger(Ledger ledger, Pageable pageable);

    List<PaymentCategoryDTO> findAllByLedger(Ledger ledger);

    List<PaymentCategoryDTO> findAllShowByLedger(Ledger ledger);

    Optional<PaymentCategoryDTO> findOneByLedger(Ledger ledger, Long id);

    void deleteByLedger(Ledger ledger, Long id);

    void hiddenByLedger(Ledger ledger, Long id);

    void unhiddenByLedger(Ledger ledger, Long id);

    List<PaymentCategoryDTO> findAllExistingByLedger(Ledger ledger);
}
