package com.produck.web.rest;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.repository.LedgerRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.HomeService;
import com.produck.service.UserService;
import com.produck.service.dto.HomeDTO;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Home Dashboard.
 */
@RestController
@RequestMapping("/api")
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(HomeResource.class);

    private static final String ENTITY_NAME = "ledger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomeService homeService;

    private final LedgerRepository ledgerRepository;

    private final UserService userService;

    public HomeResource(HomeService homeService, LedgerRepository ledgerRepository, UserService userService) {
        this.homeService = homeService;
        this.ledgerRepository = ledgerRepository;
        this.userService = userService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<HomeDTO> getHomeDashboard(@RequestParam(value = "ledgerId", required = false) Long ledgerId) {
        log.debug("REST request to get a page of Dashboard By Current User");
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        User loggedUser = user.get();
        Optional<Ledger> ledger = ledgerRepository.findOneByUserAndIsDefaultIsTrue(loggedUser);
        if (ledger.isEmpty()) {
            throw new RuntimeException("No ledger was found");
        }
        Ledger selectLedger = ledger.get();
        if (Objects.nonNull(ledgerId)) {
            Optional<Ledger> selectLedgerOptional = ledgerRepository.findOneByUserAndId(loggedUser, ledgerId);
            if (selectLedgerOptional.isEmpty()) {
                throw new RuntimeException("No selected ledger was found");
            }
            selectLedger = selectLedgerOptional.get();
        }
        HomeDTO home = homeService.getHomeDashboard(loggedUser, selectLedger);
        return ResponseEntity.ok().body(home);
    }
}
