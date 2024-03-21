package com.produck.repository;

import com.produck.domain.SplitBookDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SplitBookDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SplitBookDetailRepository extends JpaRepository<SplitBookDetail, Long> {}
