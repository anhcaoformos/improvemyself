package com.produck.web.rest;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.repository.LedgerRepository;
import com.produck.repository.TransactionRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.TransactionService;
import com.produck.service.UserService;
import com.produck.service.dto.SummaryPaymentCategoryDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SummaryResource {

    private final Logger log = LoggerFactory.getLogger(SummaryResource.class);

    private static final String ENTITY_NAME = "summary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final TransactionService transactionService;

    private final LedgerRepository ledgerRepository;

    private final TransactionRepository transactionRepository;

    public SummaryResource(
        UserService userService,
        TransactionService transactionService,
        LedgerRepository ledgerRepository,
        TransactionRepository transactionRepository
    ) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.ledgerRepository = ledgerRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/user/ledger/{ledgerId}/summary-payment-categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<SummaryPaymentCategoryDTO>> getSummaryPaymentCategoriesByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @RequestParam("month") LocalDate month,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Summary Payment Categories By Ledger: {} and Month: {}", ledgerId, month);
        Ledger ledger = validateAndGetLedger(ledgerId);
        List<SummaryPaymentCategoryDTO> summaryPaymentCategories = transactionService.findSummaryPaymentCategories(ledger, month);
        return ResponseEntity.ok(summaryPaymentCategories);
    }

    private Ledger validateAndGetLedger(Long ledgerId) {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<Ledger> ledger = ledgerRepository.findOneByUserAndId(user.get(), ledgerId);
        if (ledger.isEmpty()) {
            throw new RuntimeException("No ledger was found");
        }
        return ledger.get();
    }

    private Ledger validateAndGetDefaultLedger() {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<Ledger> ledger = ledgerRepository.findOneByUserAndIsDefaultIsTrue(user.get());
        if (ledger.isEmpty()) {
            throw new RuntimeException("No ledger was found");
        }
        return ledger.get();
    }
}
