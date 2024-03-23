package com.produck.web.rest;

import com.produck.config.Constants;
import com.produck.domain.Ledger;
import com.produck.domain.Transaction;
import com.produck.domain.User;
import com.produck.domain.enumeration.TransactionType;
import com.produck.repository.LedgerRepository;
import com.produck.repository.TransactionRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.TransactionService;
import com.produck.service.UserService;
import com.produck.service.dto.TransactionDTO;
import com.produck.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.produck.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final LedgerRepository ledgerRepository;

    public TransactionResource(
        TransactionService transactionService,
        TransactionRepository transactionRepository,
        UserService userService,
        LedgerRepository ledgerRepository
    ) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transactionDTO the transactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionDTO, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transactionDTO);
        if (transactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions/:id} : Updates an existing transaction.
     *
     * @param id the id of the transactionDTO to save.
     * @param transactionDTO the transactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionDTO,
     * or with status {@code 400 (Bad Request)} if the transactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionDTO result = transactionService.update(transactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transactions/:id} : Partial updates given fields of an existing transaction, field will ignore if it is null
     *
     * @param id the id of the transactionDTO to save.
     * @param transactionDTO the transactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionDTO,
     * or with status {@code 400 (Bad Request)} if the transactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionDTO> partialUpdateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaction partially : {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionDTO> result = transactionService.partialUpdate(transactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transactions");
        Page<TransactionDTO> page = transactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<TransactionDTO> transactionDTO = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionDTO);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/transactions")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Payment Categories By User");
        Ledger ledger = validateAndGetLedger();
        Page<TransactionDTO> page = transactionService.findAllByLedger(ledger, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/ledger/transactions")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> createTransactionByUser(@RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction By User: {}", transactionDTO);
        if (transactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validateAndGetDefaultLedger(transactionDTO.getId());
        if (
            Objects.nonNull(transactionDTO.getPaymentCategory()) &&
            Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
        ) {
            if (
                TransactionType.INCOME.equals(transactionDTO.getTransactionType()) &&
                Constants.EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
            if (
                TransactionType.EXPENSE.equals(transactionDTO.getTransactionType()) &&
                Constants.INCOME_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> updateTransactionByUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction By User: {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger(id);
        if (
            Objects.nonNull(transactionDTO.getPaymentCategory()) &&
            Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
        ) {
            if (
                TransactionType.INCOME.equals(transactionDTO.getTransactionType()) &&
                Constants.EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
            if (
                TransactionType.EXPENSE.equals(transactionDTO.getTransactionType()) &&
                Constants.INCOME_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
        }
        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionDTO result = transactionService.update(transactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> partialUpdateTransactionByUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaction partially By User: {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger(id);
        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionDTO> result = transactionService.partialUpdate(transactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> getTransactionByUser(@PathVariable Long id) {
        log.debug("REST request to get Transaction By User: {}", id);
        Ledger ledger = validateAndGetDefaultLedger(id);
        Optional<TransactionDTO> transactionDTO = transactionService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(transactionDTO);
    }

    @DeleteMapping("/user/ledger/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteTransactionByUser(@PathVariable Long id) {
        log.debug("REST request to delete Transaction By User: {}", id);
        Ledger ledger = validateAndGetDefaultLedger(id);
        transactionService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/{ledgerId}/transactions")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @RequestParam(value = "transactionDate", required = false) LocalDate transactionDate,
        @RequestParam(value = "type", required = false) TransactionType type,
        @RequestParam(value = "paymentCategoryId", required = false) Long paymentCategoryId,
        @RequestParam(value = "paymentMethodId", required = false) Long paymentMethodId,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Payment Categories By Ledger");
        Ledger ledger = validateAndGetLedger(ledgerId);
        Page<TransactionDTO> page = transactionService.findAllBy(
            ledger,
            transactionDate,
            type,
            paymentCategoryId,
            paymentMethodId,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/ledger/{ledgerId}/transactions")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> createTransactionByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Transaction By Ledger: {}", transactionDTO);
        if (transactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validateAndGetLedger(ledgerId);
        if (
            Objects.nonNull(transactionDTO.getPaymentCategory()) &&
            Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
        ) {
            if (
                TransactionType.INCOME.equals(transactionDTO.getTransactionType()) &&
                Constants.EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
            if (
                TransactionType.EXPENSE.equals(transactionDTO.getTransactionType()) &&
                Constants.INCOME_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/" + ledgerId + "/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/{ledgerId}/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> updateTransactionByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction By Ledger: {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (
            Objects.nonNull(transactionDTO.getPaymentCategory()) &&
            Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
        ) {
            if (
                TransactionType.INCOME.equals(transactionDTO.getTransactionType()) &&
                Constants.EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
            if (
                TransactionType.EXPENSE.equals(transactionDTO.getTransactionType()) &&
                Constants.INCOME_PAY_BOOK_PAYMENT_CATEGORIES.contains(transactionDTO.getPaymentCategory().getCode())
            ) {
                throw new BadRequestAlertException("Invalid transaction type and payment category", ENTITY_NAME, "invalidTypeAndCategory");
            }
        }
        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionDTO result = transactionService.update(transactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/{ledgerId}/transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> partialUpdateTransactionByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionDTO transactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaction partially By Ledger: {}, {}", id, transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionDTO> result = transactionService.partialUpdate(transactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/{ledgerId}/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<TransactionDTO> getTransactionByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to get Transaction By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        Optional<TransactionDTO> transactionDTO = transactionService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(transactionDTO);
    }

    @DeleteMapping("/user/ledger/{ledgerId}/transactions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteTransactionByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to delete Transaction By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        transactionService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private Ledger validateAndGetLedger() {
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

    private Ledger validateAndGetDefaultLedger(Long transactionId) {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        if (Objects.isNull(transactionId)) {
            Optional<Ledger> ledger = ledgerRepository.findOneByUserAndIsDefaultIsTrue(user.get());
            if (ledger.isEmpty()) {
                throw new RuntimeException("No ledger was found");
            }
            return ledger.get();
        }

        Optional<Transaction> transaction = transactionRepository.findOneByUserAndId(user.get(), transactionId);
        if (transaction.isEmpty()) {
            throw new RuntimeException("No transaction was found");
        }
        return transaction.get().getLedger();
    }
}
