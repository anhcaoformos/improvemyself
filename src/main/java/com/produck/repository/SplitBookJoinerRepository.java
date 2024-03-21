package com.produck.repository;

import com.produck.domain.SplitBookJoiner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SplitBookJoiner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SplitBookJoinerRepository extends JpaRepository<SplitBookJoiner, Long> {}
