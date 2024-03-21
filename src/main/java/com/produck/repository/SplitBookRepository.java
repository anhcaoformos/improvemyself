package com.produck.repository;

import com.produck.domain.SplitBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SplitBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SplitBookRepository extends JpaRepository<SplitBook, Long> {}
