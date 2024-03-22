package com.produck.repository;

import com.produck.domain.SplitBook;
import com.produck.domain.SplitBookDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SplitBookDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SplitBookDetailRepository extends JpaRepository<SplitBookDetail, Long> {
    Page<SplitBookDetail> findAllBySplitBook(SplitBook splitBook, Pageable pageable);
}
