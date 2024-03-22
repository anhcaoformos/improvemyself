package com.produck.repository;

import com.produck.domain.SplitBook;
import com.produck.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SplitBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SplitBookRepository extends JpaRepository<SplitBook, Long> {
    Page<SplitBook> findAllByUser(User user, Pageable pageable);

    Optional<SplitBook> findOneByUserAndId(User user, Long id);

    void deleteByUserAndId(User user, Long id);
}
