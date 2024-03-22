package com.produck.repository;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ledger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Page<Ledger> findAllByUser(User user, Pageable pageable);
    List<Ledger> findAllByUser(User user);

    Optional<Ledger> findOneByUserAndId(User user, Long id);

    Optional<Ledger> findOneByUserAndIsDefaultIsTrue(User user);

    void deleteByUserAndId(User user, Long id);

    @Modifying
    @Query("update Ledger leger set leger.isDefault = false where leger.user = :user")
    void resetDefaultByUserAndId(User user);

    @Modifying
    @Query("update Ledger leger set leger.isDefault = true where leger.user = :user and leger.id = :id")
    void setDefaultByUserAndId(User user, Long id);

    boolean existsByUserAndIdAndIsDefaultIsTrue(User user, Long ledgerId);
}
