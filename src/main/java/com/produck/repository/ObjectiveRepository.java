package com.produck.repository;

import com.produck.domain.Ledger;
import com.produck.domain.Objective;
import com.produck.domain.PaymentCategory;
import com.produck.domain.enumeration.ObjectiveType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Objective entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Page<Objective> findAllByLedger(Ledger ledger, Pageable pageable);

    List<Objective> findAllByLedger(Ledger ledger);

    List<Objective> findAllByLedgerAndTypeAndIsHiddenIsFalse(Ledger ledger, ObjectiveType type);

    List<Objective> findAllByLedgerAndPaymentCategory(Ledger ledger, PaymentCategory paymentCategory);

    List<Objective> findAllByLedgerAndPaymentCategoryAndTypeAndIsHiddenIsFalse(
        Ledger ledger,
        PaymentCategory paymentCategory,
        ObjectiveType type
    );

    List<Objective> findAllByLedgerAndPaymentCategoryCodeInAndIsHiddenIsFalse(Ledger ledger, List<String> paymentCategories);

    List<Objective> findAllByLedgerAndIsHiddenIsFalse(Ledger ledger);

    Optional<Objective> findOneByLedgerAndId(Ledger ledger, Long id);

    @Query("select o from Objective o where o.ledger = :ledger and o.type = 'DEFAULT'")
    Optional<Objective> findDefaultObjective(Ledger ledger);

    void deleteByLedgerAndId(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);

    @Modifying
    @Query("update Objective ct set ct.isHidden = true where ct.ledger = :ledger and ct.id = :id")
    void hiddenByLedgerAndId(Ledger ledger, Long id);

    @Modifying
    @Query("update Objective ct set ct.isHidden = false where ct.ledger = :ledger and ct.id = :id")
    void unhiddenByLedgerAndId(Ledger ledger, Long id);

    Optional<Objective> findByLedgerAndPaymentCategoryAndNameAndType(
        Ledger ledger,
        PaymentCategory paymentCategory,
        String name,
        ObjectiveType type
    );
}
