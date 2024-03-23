package com.produck.web.rest;

import com.produck.domain.User;
import com.produck.repository.LedgerRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.LedgerService;
import com.produck.service.UserService;
import com.produck.service.dto.LedgerDTO;
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
 * REST controller for managing {@link com.produck.domain.Ledger}.
 */
@RestController
@RequestMapping("/api")
public class LedgerResource {

    private final Logger log = LoggerFactory.getLogger(LedgerResource.class);

    private static final String ENTITY_NAME = "ledger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LedgerService ledgerService;

    private final LedgerRepository ledgerRepository;

    private final UserService userService;

    public LedgerResource(LedgerService ledgerService, LedgerRepository ledgerRepository, UserService userService) {
        this.ledgerService = ledgerService;
        this.ledgerRepository = ledgerRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /ledgers} : Create a new ledger.
     *
     * @param ledgerDTO the ledgerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ledgerDTO, or with status {@code 400 (Bad Request)} if the ledger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ledgers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<LedgerDTO> createLedger(@RequestBody LedgerDTO ledgerDTO) throws URISyntaxException {
        log.debug("REST request to save Ledger : {}", ledgerDTO);
        if (ledgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new ledger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        LedgerDTO result = ledgerService.save(user.get(), ledgerDTO);
        return ResponseEntity.created(new URI("/api/ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ledgers/:id} : Updates an existing ledger.
     *
     * @param id the id of the ledgerDTO to save.
     * @param ledgerDTO the ledgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerDTO,
     * or with status {@code 400 (Bad Request)} if the ledgerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ledgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<LedgerDTO> updateLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerDTO ledgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ledger : {}, {}", id, ledgerDTO);
        if (ledgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LedgerDTO result = ledgerService.update(ledgerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ledgerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ledgers/:id} : Partial updates given fields of an existing ledger, field will ignore if it is null
     *
     * @param id the id of the ledgerDTO to save.
     * @param ledgerDTO the ledgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerDTO,
     * or with status {@code 400 (Bad Request)} if the ledgerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ledgerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ledgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ledgers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<LedgerDTO> partialUpdateLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerDTO ledgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ledger partially : {}, {}", id, ledgerDTO);
        if (ledgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LedgerDTO> result = ledgerService.partialUpdate(ledgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ledgerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ledgers} : get all the ledgers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ledgers in body.
     */
    @GetMapping("/ledgers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<LedgerDTO>> getAllLedgers(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ledgers");
        Page<LedgerDTO> page = ledgerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ledgers/:id} : get the "id" ledger.
     *
     * @param id the id of the ledgerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ledgerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<LedgerDTO> getLedger(@PathVariable Long id) {
        log.debug("REST request to get Ledger : {}", id);
        Optional<LedgerDTO> ledgerDTO = ledgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ledgerDTO);
    }

    /**
     * {@code DELETE  /ledgers/:id} : delete the "id" ledger.
     *
     * @param id the id of the ledgerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteLedger(@PathVariable Long id) {
        log.debug("REST request to delete Ledger : {}", id);
        ledgerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<LedgerDTO> getDefaultLedgerByCurrentUser() {
        log.debug("REST request to get a Default Ledger By Current User");
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<LedgerDTO> ledgerDTO = ledgerService.findDefaultByUser(user.get());
        return ResponseUtil.wrapOrNotFound(ledgerDTO);
    }

    @GetMapping("/user/ledgers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<LedgerDTO>> getAllLedgersByCurrentUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ledgers By Current User");
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Page<LedgerDTO> page = ledgerService.findAllByUser(user.get(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/ledgers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<LedgerDTO> createLedgerByCurrentUser(@RequestBody LedgerDTO ledgerDTO) throws URISyntaxException {
        log.debug("REST request to save Ledger By Current User: {}", ledgerDTO);
        if (ledgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new ledger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        LedgerDTO result = ledgerService.save(user.get(), ledgerDTO);
        return ResponseEntity.created(new URI("/api/ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<LedgerDTO> updateLedgerByCurrentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerDTO ledgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ledger By Current User: {}, {}", id, ledgerDTO);
        if (ledgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        if (!user.get().getId().equals(ledgerDTO.getUser().getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!ledgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LedgerDTO result = ledgerService.update(ledgerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ledgerDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledgers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<LedgerDTO> partialUpdateLedgerByCurrentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerDTO ledgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ledger partially By Current User: {}, {}", id, ledgerDTO);
        if (ledgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        if (!user.get().getId().equals(ledgerDTO.getUser().getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!ledgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LedgerDTO> result = ledgerService.partialUpdate(ledgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ledgerDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<LedgerDTO> getLedgerByCurrentUser(@PathVariable Long id) {
        log.debug("REST request to get Ledger By Current User: {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<LedgerDTO> ledgerDTO = ledgerService.findOneByUser(user.get(), id);
        return ResponseUtil.wrapOrNotFound(ledgerDTO);
    }

    @DeleteMapping("/user/ledgers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteLedgerByCurrentUser(@PathVariable Long id) {
        log.debug("REST request to delete Ledger By Current User: {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        ledgerService.deleteByUser(user.get(), id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user/ledgers/set-default/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> setDefaultByCurrentUser(@PathVariable(value = "id") final Long id) throws URISyntaxException {
        log.debug("REST request to set default Ledger : {}", id);

        if (!ledgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        ledgerService.setDefault(user.get(), id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
