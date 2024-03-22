package com.produck.web.rest;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.repository.LedgerRepository;
import com.produck.repository.PaymentMethodRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.PaymentMethodService;
import com.produck.service.UserService;
import com.produck.service.dto.PaymentMethodDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.produck.domain.PaymentMethod}.
 */
@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private static final String ENTITY_NAME = "paymentMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentMethodService paymentMethodService;

    private final PaymentMethodRepository paymentMethodRepository;

    private final UserService userService;

    private final LedgerRepository ledgerRepository;

    public PaymentMethodResource(
        PaymentMethodService paymentMethodService,
        PaymentMethodRepository paymentMethodRepository,
        UserService userService,
        LedgerRepository ledgerRepository
    ) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.userService = userService;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * {@code POST  /payment-methods} : Create a new paymentMethod.
     *
     * @param paymentMethodDTO the paymentMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMethodDTO, or with status {@code 400 (Bad Request)} if the paymentMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-methods")
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod : {}", paymentMethodDTO);
        if (paymentMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentMethodDTO result = paymentMethodService.save(paymentMethodDTO);
        return ResponseEntity.created(new URI("/api/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-methods/:id} : Updates an existing paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to save.
     * @param paymentMethodDTO the paymentMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethodDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-methods/{id}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod : {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentMethodDTO result = paymentMethodService.update(paymentMethodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-methods/:id} : Partial updates given fields of an existing paymentMethod, field will ignore if it is null
     *
     * @param id the id of the paymentMethodDTO to save.
     * @param paymentMethodDTO the paymentMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethodDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMethodDTO> partialUpdatePaymentMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentMethod partially : {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMethodDTO> result = paymentMethodService.partialUpdate(paymentMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-methods} : get all the paymentMethods.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMethods in body.
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaymentMethods");
        Page<PaymentMethodDTO> page = paymentMethodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-methods/:id} : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-methods/{id}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMethodDTO);
    }

    /**
     * {@code DELETE  /payment-methods/:id} : delete the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-methods/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        paymentMethodService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/payment-methods")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethodsByDefaultLedger(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Payment Methods By Default Ledger");
        Ledger ledger = validateAndGetDefaultLedger();
        Page<PaymentMethodDTO> page = paymentMethodService.findAllByLedger(ledger, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/ledger/payment-methods")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> createPaymentMethodByDefaultLedger(@RequestBody PaymentMethodDTO paymentMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentMethod By Default Ledger: {}", paymentMethodDTO);
        if (paymentMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ledger ledger = validateAndGetDefaultLedger();
        if (Boolean.TRUE.equals(paymentMethodRepository.existsByLedgerAndCodeAndIsHiddenIsFalse(ledger, paymentMethodDTO.getCode()))) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an Code", ENTITY_NAME, "codeexists");
        }
        PaymentMethodDTO result = paymentMethodService.saveByLedger(ledger, paymentMethodDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethodByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod By Default Ledger: {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentMethodDTO result = paymentMethodService.update(paymentMethodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/payment-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> partialUpdatePaymentMethodByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentMethod partially By Default Ledger: {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMethodDTO> result = paymentMethodService.partialUpdate(paymentMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(paymentMethodDTO);
    }

    @DeleteMapping("/user/ledger/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deletePaymentMethodByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        paymentMethodService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/{ledgerId}/payment-methods")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethodsByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Payment Methods By Ledger");
        Ledger ledger = validateAndGetLedger(ledgerId);
        Page<PaymentMethodDTO> page = paymentMethodService.findAllByLedger(ledger, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/ledger/{ledgerId}/payment-methods")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> createPaymentMethodByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod By Ledger: {}", paymentMethodDTO);
        if (paymentMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ledger ledger = validateAndGetLedger(ledgerId);
        if (Boolean.TRUE.equals(paymentMethodRepository.existsByLedgerAndCodeAndIsHiddenIsFalse(ledger, paymentMethodDTO.getCode()))) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an Code", ENTITY_NAME, "codeexists");
        }
        PaymentMethodDTO result = paymentMethodService.saveByLedger(ledger, paymentMethodDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/" + ledgerId + "/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/{ledgerId}/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethodByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod By Ledger: {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentMethodDTO result = paymentMethodService.update(paymentMethodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/{ledgerId}/payment-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> partialUpdatePaymentMethodByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentMethodDTO paymentMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentMethod partially By Ledger: {}, {}", id, paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!paymentMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMethodDTO> result = paymentMethodService.partialUpdate(paymentMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/{ledgerId}/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to get PaymentMethod By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(paymentMethodDTO);
    }

    @DeleteMapping("/user/ledger/{ledgerId}/payment-methods/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<PaymentMethodDTO>> deletePaymentMethodByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete PaymentMethod By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        paymentMethodService.deleteByLedger(ledger, id);
        List<PaymentMethodDTO> paymentMethods = paymentMethodService.findAllByLedger(ledger);
        return ResponseEntity.ok(paymentMethods);
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
