package com.produck.repository;

import com.produck.domain.Goal;
import com.produck.domain.Ledger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Goal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByLedger(Ledger ledger, Pageable pageable);

    List<Goal> findAllByLedger(Ledger ledger);

    Optional<Goal> findOneByLedgerAndId(Ledger ledger, Long id);

    void deleteByLedgerAndId(Ledger ledger, Long id);

    void deleteAllByLedgerId(Long ledgerId);
}
