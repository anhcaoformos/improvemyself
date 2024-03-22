package com.produck.service;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentCategory;
import com.produck.domain.PaymentMethod;
import com.produck.domain.enumeration.TransactionType;
import com.produck.service.dto.AssetBalanceDTO;
import com.produck.service.dto.ObjectivePayBookDTO;
import com.produck.service.dto.SummaryPaymentCategoryDTO;
import com.produck.service.dto.TransactionDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.produck.domain.Transaction}.
 */
public interface TransactionService {
    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionDTO save(TransactionDTO transactionDTO);

    /**
     * Updates a transaction.
     *
     * @param transactionDTO the entity to update.
     * @return the persisted entity.
     */
    TransactionDTO update(TransactionDTO transactionDTO);

    /**
     * Partially updates a transaction.
     *
     * @param transactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionDTO> partialUpdate(TransactionDTO transactionDTO);

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionDTO> findOne(Long id);

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<TransactionDTO> findAllByLedger(Ledger ledger, Pageable pageable);

    Page<TransactionDTO> findAllBy(
        Ledger ledger,
        LocalDate transactionDate,
        TransactionType type,
        Long paymentCategoryId,
        Long paymentMethodId,
        Pageable pageable
    );

    Optional<TransactionDTO> findOneByLedger(Ledger ledger, Long id);

    void deleteByLedger(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);

    Page<AssetBalanceDTO> findAllAssetBalanceByLedger(Ledger ledger, Pageable pageable);

    Page<ObjectivePayBookDTO> findAllObjectivePayBookByLedger(Ledger ledger, Pageable pageable);

    List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Ledger ledger);

    List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Ledger ledger, LocalDate month);

    List<LocalDate> getFilterTransactionDates(Ledger ledger);

    void createInitTransaction(Ledger ledger, PaymentMethod paymentMethod, PaymentCategory paymentCategory);
}
