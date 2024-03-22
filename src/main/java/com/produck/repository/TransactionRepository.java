package com.produck.repository;

import com.produck.domain.Ledger;
import com.produck.domain.Transaction;
import com.produck.domain.User;
import com.produck.domain.enumeration.TransactionType;
import com.produck.service.dto.AssetBalanceDTO;
import com.produck.service.dto.ObjectivePayBookDTO;
import com.produck.service.dto.SummaryPaymentCategoryDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByLedger(Ledger ledger, Pageable pageable);

    @Query(
        "select tran from Transaction tran where tran.ledger = :ledger " +
        "and (:transactionDate is null or tran.transactionDate = :transactionDate)" +
        "and (:type is null or tran.transactionType = :type)" +
        "and (:paymentCategoryId is null or tran.paymentCategory.id = :paymentCategoryId)" +
        "and (:paymentMethodId is null or tran.paymentMethod.id = :paymentMethodId)"
    )
    Page<Transaction> findAllBy(
        Ledger ledger,
        LocalDate transactionDate,
        TransactionType type,
        Long paymentCategoryId,
        Long paymentMethodId,
        Pageable pageable
    );

    Optional<Transaction> findOneByLedgerAndId(Ledger ledger, Long id);

    @Query("select transaction from Transaction  transaction where transaction.id = :id and transaction.ledger.user = :user")
    Optional<Transaction> findOneByUserAndId(User user, Long id);

    void deleteByLedgerAndId(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);

    @Query(
        "select tran from Transaction tran where tran.ledger = :ledger and tran.objective.id = :objectiveId and tran.objective.type = 'FROM_OBJECTIVE'"
    )
    boolean existsByLedgerAndFromObjectiveId(Ledger ledger, Long objectiveId);

    @Query(
        "select tran from Transaction tran where tran.ledger = :ledger and tran.objective.id = :objectiveId and tran.objective.type = 'TO_OBJECTIVE'"
    )
    boolean existsByLedgerAndToObjectiveId(Ledger ledger, Long objectiveId);

    boolean existsByLedgerAndPaymentMethodId(Ledger ledger, Long paymentMethodId);

    boolean existsByLedgerAndPaymentCategoryId(Ledger ledger, Long id);

    @Query(
        nativeQuery = true,
        value = "((SELECT CONCAT(tran.ledger_id, '_', tran.payment_method_id) as id, " +
        "COALESCE(SUM(CASE WHEN tran.transaction_type = 'INCOME' THEN tran.amount " +
        "WHEN tran.transaction_type IN ('EXPENSE', 'INTERNAL') THEN -tran.amount END), 0) " +
        "+ COALESCE((SELECT SUM(tran2.amount) FROM transaction tran2 " +
        "WHERE tran.payment_method_id = tran2.target_payment_method_id AND tran2.ledger_id = :ledgerId " +
        "GROUP BY tran2.target_payment_method_id, tran2.ledger_id), 0) as totalAmount, " +
        "tran.ledger_id as ledgerId, " +
        "pm.name as paymentMethodName " +
        "FROM transaction tran JOIN payment_method pm on tran.payment_method_id = pm.id " +
        "WHERE tran.ledger_id = :ledgerId " +
        "GROUP BY tran.payment_method_id, tran.ledger_id))",
        countQuery = "SELECT COUNT(*) FROM transaction tran WHERE tran.ledger_id = :ledgerId GROUP BY tran.payment_method_id, tran.ledger_id"
    )
    Page<AssetBalanceDTO> findCurrentAssetBalanceByLedger(Long ledgerId, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "((SELECT CONCAT(tran.ledger_id, '_', tran.payment_method_id) as id, " +
        "COALESCE(SUM(CASE WHEN tran.transaction_type = 'INCOME' THEN tran.amount " +
        "WHEN tran.transaction_type IN ('EXPENSE', 'INTERNAL') THEN -tran.amount END), 0) " +
        "+ COALESCE((SELECT SUM(tran2.amount) FROM transaction tran2 " +
        "WHERE tran.payment_method_id = tran2.target_payment_method_id AND tran2.ledger_id = :ledgerId " +
        "GROUP BY tran2.target_payment_method_id, tran2.ledger_id), 0) as totalAmount, " +
        "tran.ledger_id as ledgerId, " +
        "pm.name as paymentMethodName " +
        "FROM transaction tran JOIN payment_method pm on tran.payment_method_id = pm.id " +
        "WHERE tran.ledger_id = :ledgerId and tran.transaction_date >= :fromDate and tran.transaction_date <= :toDate group by tran.payment_pethod_id, tran.ledger_id))"
    )
    Page<AssetBalanceDTO> findAssetBalanceByLedgerAndPeriod(Long ledgerId, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "((SELECT CONCAT(tran.ledger_id, '_', tran.payment_method_id) as id, " +
        "COALESCE(SUM(CASE WHEN tran.transaction_type = 'INCOME' THEN tran.amount " +
        "WHEN tran.transaction_type IN ('EXPENSE', 'INTERNAL') THEN -tran.amount END), 0) " +
        "+ COALESCE((SELECT SUM(tran2.amount) FROM transaction tran2 " +
        "WHERE tran.payment_method_id = tran2.target_payment_method_id AND tran2.ledger_id = :ledgerId " +
        "GROUP BY tran2.target_payment_method_id, tran2.ledger_id), 0) as totalAmount, " +
        "tran.ledger_id as ledgerId, " +
        "pm.name as paymentMethodName " +
        "FROM transaction tran JOIN payment_method pm on tran.payment_method_id = pm.id " +
        "WHERE tran.ledger_id = :ledgerId and tran.transaction_date >= :fromDate and tran.transaction_date <= :toDate " +
        "group by DATE_FORMAT(tran.transaction_date, '%Y-%m'), tran.payment_method_id, tran.ledger_id))"
    )
    Page<AssetBalanceDTO> findAssetBalanceByLedgerAndPeriodInMonth(Long ledgerId, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "((select tran.transaction_date as transactionDate, o.name as objectiveName," +
        "coalesce(sum(case when tran.transaction_type = 'INCOME' then -tran.amount when tran.transaction_type = 'EXPENSE' then tran.amount end),0) as amount, " +
        "tran.ledger_id as ledgerId from transaction tran join payment_category pc on tran.payment_category_id = pc.id join objective o on tran.objective_id = o.id " +
        "where tran.ledger_id = :ledgerId and pc.code in ('LOAN', 'BORROW', 'RECEIVED', 'PAID') " +
        "group by tran.objective_id, tran.transaction_date, tran.ledger_id))"
    )
    Page<ObjectivePayBookDTO> findObjectivePayBookByLedger(Long ledgerId, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "select tran.ledger_id as ledgerId, pc.name as name, tran.transaction_type as type , sum(tran.amount) as totalAmount from `transaction` tran join payment_category pc on tran.payment_category_id = pc.id where tran.ledger_id = :ledgerId group by tran.ledger_id , tran.payment_category_id, tran.transaction_type"
    )
    List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Long ledgerId);

    @Query(
        nativeQuery = true,
        value = "select tran.ledger_id as ledgerId, pc.name as name, tran.transaction_type as type , sum(tran.amount) as totalAmount from `transaction` tran join payment_category pc on tran.payment_category_id = pc.id where tran.ledger_id = :ledgerId and YEAR(tran.transaction_date) = YEAR(:month) AND MONTH(tran.transaction_date) = MONTH(:month) group by tran.ledger_id , tran.payment_category_id, tran.transaction_type"
    )
    List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Long ledgerId, LocalDate month);

    Optional<Transaction> getFirstByLedgerOrderByTransactionDate(Ledger ledger);
}
