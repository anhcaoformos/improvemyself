package com.produck.repository;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCategoryRepository extends JpaRepository<PaymentCategory, Long> {
    Page<PaymentCategory> findAllByLedger(Ledger ledger, Pageable pageable);

    List<PaymentCategory> findAllByLedger(Ledger ledger);

    @Query("select distinct(tran.paymentCategory) from Transaction tran where tran.ledger = :ledger and tran.paymentCategory is not null")
    List<PaymentCategory> findAllExistingByLedger(Ledger ledger);

    List<PaymentCategory> findAllByLedgerAndIsHiddenIsFalse(Ledger ledger);

    Optional<PaymentCategory> findOneByLedgerAndId(Ledger ledger, Long id);

    void deleteByLedgerAndId(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);

    @Modifying
    @Query("update PaymentCategory pc set pc.isHidden = true where pc.ledger = :ledger and pc.id = :id")
    void hiddenByLedgerAndId(Ledger ledger, Long id);

    @Modifying
    @Query("update PaymentCategory pc set pc.isHidden = false where pc.ledger = :ledger and pc.id = :id")
    void unhiddenByLedgerAndId(Ledger ledger, Long id);

    Boolean existsByLedgerAndCode(Ledger ledger, String code);

    Optional<PaymentCategory> findByLedgerAndCode(Ledger ledger, String code);
}
