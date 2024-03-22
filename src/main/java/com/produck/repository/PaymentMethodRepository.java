package com.produck.repository;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentMethod;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Page<PaymentMethod> findAllByLedgerAndIsHiddenIsFalse(Ledger ledger, Pageable pageable);

    List<PaymentMethod> findAllByLedgerAndIsHiddenIsFalse(Ledger ledger);

    @Query("select distinct(tran.paymentMethod) from Transaction tran where tran.ledger = :ledger and tran.paymentMethod is not null")
    List<PaymentMethod> findAllExistingByLedger(Ledger ledger);

    Optional<PaymentMethod> findOneByLedgerAndIdAndIsHiddenIsFalse(Ledger ledger, Long id);

    void deleteByLedgerAndId(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);

    Boolean existsByLedgerAndCodeAndIsHiddenIsFalse(Ledger ledger, String code);

    @Modifying
    @Query("update PaymentMethod pm set pm.isHidden = true where pm.ledger = :ledger and pm.id = :id")
    void hiddenByLedgerAndId(Ledger ledger, Long id);

    @Modifying
    @Query("update PaymentMethod pm set pm.isHidden = false where pm.ledger = :ledger and pm.id = :id")
    void unhiddenByLedgerAndId(Ledger ledger, Long id);
}
