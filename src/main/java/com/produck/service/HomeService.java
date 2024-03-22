package com.produck.service;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.service.dto.HomeDTO;

/**
 * Service Interface for managing {@link Ledger}.
 */
public interface HomeService {
    HomeDTO getHomeDashboard(User user, Ledger ledger);
}
