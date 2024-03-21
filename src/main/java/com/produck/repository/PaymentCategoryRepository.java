package com.produck.repository;

import com.produck.domain.PaymentCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCategoryRepository extends JpaRepository<PaymentCategory, Long> {}
