package com.produck.web.rest;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.repository.LedgerRepository;
import com.produck.repository.PaymentCategoryRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.PaymentCategoryService;
import com.produck.service.UserService;
import com.produck.service.dto.PaymentCategoryDTO;
import com.produck.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.produck.domain.PaymentCategory}.
 */
@RestController
@RequestMapping("/api/payment-categories")
public class PaymentCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryResource.class);

    private static final String ENTITY_NAME = "paymentCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCategoryService paymentCategoryService;

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final UserService userService;

    private final LedgerRepository ledgerRepository;

    public PaymentCategoryResource(
        PaymentCategoryService paymentCategoryService,
        PaymentCategoryRepository paymentCategoryRepository,
        UserService userService,
        LedgerRepository ledgerRepository
    ) {
        this.paymentCategoryService = paymentCategoryService;
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.userService = userService;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * {@code POST  /payment-categories} : Create a new paymentCategory.
     *
     * @param paymentCategoryDTO the paymentCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCategoryDTO, or with status {@code 400 (Bad Request)} if the paymentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-categories")
    public ResponseEntity<PaymentCategoryDTO> createPaymentCategory(@RequestBody PaymentCategoryDTO paymentCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentCategory : {}", paymentCategoryDTO);
        if (paymentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCategoryDTO result = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity.created(new URI("/api/payment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-categories/:id} : Updates an existing paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to save.
     * @param paymentCategoryDTO the paymentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-categories/{id}")
    public ResponseEntity<PaymentCategoryDTO> updatePaymentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCategory : {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCategoryDTO result = paymentCategoryService.update(paymentCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-categories/:id} : Partial updates given fields of an existing paymentCategory, field will ignore if it is null
     *
     * @param id the id of the paymentCategoryDTO to save.
     * @param paymentCategoryDTO the paymentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentCategoryDTO> partialUpdatePaymentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCategory partially : {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCategoryDTO> result = paymentCategoryService.partialUpdate(paymentCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-categories} : get all the paymentCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCategories in body.
     */
    @GetMapping("/payment-categories")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllPaymentCategories(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaymentCategories");
        Page<PaymentCategoryDTO> page = paymentCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-categories/:id} : get the "id" paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-categories/{id}")
    public ResponseEntity<PaymentCategoryDTO> getPaymentCategory(@PathVariable Long id) {
        log.debug("REST request to get PaymentCategory : {}", id);
        Optional<PaymentCategoryDTO> paymentCategoryDTO = paymentCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentCategoryDTO);
    }

    /**
     * {@code DELETE  /payment-categories/:id} : delete the "id" paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-categories/{id}")
    public ResponseEntity<Void> deletePaymentCategory(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCategory : {}", id);
        paymentCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/payment-categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllPaymentCategoriesByDefaultLedger() {
        log.debug("REST request to get a page of Payment Categories By Default Ledger");
        Ledger ledger = validateAndGetDefaultLedger();
        return ResponseEntity.ok().body(paymentCategoryService.findAllByLedger(ledger));
    }

    @GetMapping("/user/ledger/payment-categories/show")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllShowPaymentCategoriesByDefaultLedger() {
        log.debug("REST request to get a page of show Payment Categories By Default Ledger");
        Ledger ledger = validateAndGetDefaultLedger();
        return ResponseEntity.ok().body(paymentCategoryService.findAllShowByLedger(ledger));
    }

    @PostMapping("/user/ledger/payment-categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> createPaymentCategoryByDefaultLedger(@RequestBody PaymentCategoryDTO paymentCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentCategory By Default Ledger: {}", paymentCategoryDTO);
        if (paymentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ledger ledger = validateAndGetDefaultLedger();
        if (Boolean.TRUE.equals(paymentCategoryRepository.existsByLedgerAndCode(ledger, paymentCategoryDTO.getCode()))) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an Code", ENTITY_NAME, "codeexists");
        }
        PaymentCategoryDTO result = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/payment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> updatePaymentCategoryByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCategory By Default Ledger: {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCategoryDTO result = paymentCategoryService.update(paymentCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/payment-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> partialUpdatePaymentCategoryByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCategory partially By Default Ledger: {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCategoryDTO> result = paymentCategoryService.partialUpdate(paymentCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> getPaymentCategoryByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to get PaymentCategory By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        Optional<PaymentCategoryDTO> paymentCategoryDTO = paymentCategoryService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(paymentCategoryDTO);
    }

    @DeleteMapping("/user/ledger/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deletePaymentCategoryByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCategory By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        paymentCategoryService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user/ledger/payment-categories/hidden/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> hiddenByCurrentUser(@PathVariable(value = "id") final Long id) throws URISyntaxException {
        log.debug("REST request to hidden PaymentCategory : {}", id);

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ledger ledger = validateAndGetDefaultLedger();

        paymentCategoryService.hiddenByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user/ledger/payment-categories/unhidden/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> unhiddenByCurrentUser(@PathVariable(value = "id") final Long id) throws URISyntaxException {
        log.debug("REST request to unhidden PaymentCategory : {}", id);

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ledger ledger = validateAndGetDefaultLedger();

        paymentCategoryService.unhiddenByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/{ledgerId}/payment-categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllPaymentCategoriesByLedger(@PathVariable("ledgerId") Long ledgerId) {
        log.debug("REST request to get a page of Payment Categories By Ledger");
        Ledger ledger = validateAndGetLedger(ledgerId);
        return ResponseEntity.ok().body(paymentCategoryService.findAllByLedger(ledger));
    }

    @PostMapping("/user/ledger/{ledgerId}/payment-categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> createPaymentCategoryByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PaymentCategory By Ledger: {}", paymentCategoryDTO);
        if (paymentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validateAndGetLedger(ledgerId);
        PaymentCategoryDTO result = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/" + ledgerId + "/payment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/{ledgerId}/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> updatePaymentCategoryByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCategory By Ledger: {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCategoryDTO result = paymentCategoryService.update(paymentCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(
        value = "/user/ledger/{ledgerId}/payment-categories/{id}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> partialUpdatePaymentCategoryByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCategory partially By Ledger: {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCategoryDTO> result = paymentCategoryService.partialUpdate(paymentCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/{ledgerId}/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentCategoryDTO> getPaymentCategoryByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to get PaymentCategory By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        Optional<PaymentCategoryDTO> paymentCategoryDTO = paymentCategoryService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(paymentCategoryDTO);
    }

    @DeleteMapping("/user/ledger/{ledgerId}/payment-categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentCategoryDTO>> deletePaymentCategoryByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete PaymentCategory By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        paymentCategoryService.deleteByLedger(ledger, id);
        List<PaymentCategoryDTO> paymentCategories = paymentCategoryService.findAllShowByLedger(ledger);
        return ResponseEntity.ok(paymentCategories);
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

    @GetMapping("/user/ledger/{ledgerId}/payment-categories/existing")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllExistingPaymentCategoriesByLedger(@PathVariable("ledgerId") Long ledgerId) {
        log.debug("REST request to get a page of Payment Categories By Ledger");
        Ledger ledger = validateAndGetLedger(ledgerId);
        return ResponseEntity.ok().body(paymentCategoryService.findAllExistingByLedger(ledger));
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
