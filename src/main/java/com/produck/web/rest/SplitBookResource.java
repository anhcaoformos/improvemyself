package com.produck.web.rest;

import com.produck.domain.User;
import com.produck.repository.SplitBookRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.SplitBookService;
import com.produck.service.UserService;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.utils.StringUtils;
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
 * REST controller for managing {@link com.produck.domain.SplitBook}.
 */
@RestController
@RequestMapping("/api/split-books")
public class SplitBookResource {

    private final Logger log = LoggerFactory.getLogger(SplitBookResource.class);

    private static final String ENTITY_NAME = "splitBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplitBookService splitBookService;

    private final SplitBookRepository splitBookRepository;

    private final UserService userService;

    public SplitBookResource(SplitBookService splitBookService, SplitBookRepository splitBookRepository, UserService userService) {
        this.splitBookService = splitBookService;
        this.splitBookRepository = splitBookRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /split-books} : Create a new splitBook.
     *
     * @param splitBookDTO the splitBookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splitBookDTO, or with status {@code 400 (Bad Request)} if the splitBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/split-books")
    public ResponseEntity<SplitBookDTO> createSplitBook(@RequestBody SplitBookDTO splitBookDTO) throws URISyntaxException {
        log.debug("REST request to save SplitBook : {}", splitBookDTO);
        if (splitBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SplitBookDTO result = splitBookService.save(splitBookDTO);
        return ResponseEntity.created(new URI("/api/split-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /split-books/:id} : Updates an existing splitBook.
     *
     * @param id the id of the splitBookDTO to save.
     * @param splitBookDTO the splitBookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the splitBookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/split-books/{id}")
    public ResponseEntity<SplitBookDTO> updateSplitBook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDTO splitBookDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SplitBook : {}, {}", id, splitBookDTO);
        if (splitBookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SplitBookDTO result = splitBookService.update(splitBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /split-books/:id} : Partial updates given fields of an existing splitBook, field will ignore if it is null
     *
     * @param id the id of the splitBookDTO to save.
     * @param splitBookDTO the splitBookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the splitBookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the splitBookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/split-books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SplitBookDTO> partialUpdateSplitBook(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDTO splitBookDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SplitBook partially : {}, {}", id, splitBookDTO);
        if (splitBookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplitBookDTO> result = splitBookService.partialUpdate(splitBookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /split-books} : get all the splitBooks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of splitBooks in body.
     */
    @GetMapping("/split-books")
    public ResponseEntity<List<SplitBookDTO>> getAllSplitBooks(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SplitBooks");
        Page<SplitBookDTO> page = splitBookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /split-books/:id} : get the "id" splitBook.
     *
     * @param id the id of the splitBookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the splitBookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/split-books/{id}")
    public ResponseEntity<SplitBookDTO> getSplitBook(@PathVariable Long id) {
        log.debug("REST request to get SplitBook : {}", id);
        Optional<SplitBookDTO> splitBookDTO = splitBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(splitBookDTO);
    }

    /**
     * {@code DELETE  /split-books/:id} : delete the "id" splitBook.
     *
     * @param id the id of the splitBookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/split-books/{id}")
    public ResponseEntity<Void> deleteSplitBook(@PathVariable Long id) {
        log.debug("REST request to delete SplitBook : {}", id);
        splitBookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/split-books")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<SplitBookDTO>> getAllSplitBooksByCurrentUser(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SplitBooks By Current User");
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Page<SplitBookDTO> page = splitBookService.findAllByUser(user.get(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user/split-books")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SplitBookDTO> createSplitBookByCurrentUser(@RequestBody SplitBookDTO splitBookDTO) throws URISyntaxException {
        log.debug("REST request to save SplitBook By Current User: {}", splitBookDTO);
        if (splitBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        SplitBookDTO result = splitBookService.save(user.get(), splitBookDTO);
        return ResponseEntity.created(new URI("/api/splitBooks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/split-books/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SplitBookDTO> updateSplitBookByCurrentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDTO splitBookDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SplitBook By Current User: {}, {}", id, splitBookDTO);
        if (splitBookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        if (!user.get().getId().equals(splitBookDTO.getUser().getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!splitBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SplitBookDTO result = splitBookService.update(splitBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/split-books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SplitBookDTO> partialUpdateSplitBookByCurrentUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDTO splitBookDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SplitBook partially By Current User: {}, {}", id, splitBookDTO);
        if (splitBookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        if (!user.get().getId().equals(splitBookDTO.getUser().getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!splitBookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplitBookDTO> result = splitBookService.partialUpdate(splitBookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString())
        );
    }

    @GetMapping("/user/split-books/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<SplitBookDTO> getSplitBookByCurrentUser(@PathVariable Long id) {
        log.debug("REST request to get SplitBook By Current User: {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<SplitBookDTO> splitBookDTO = splitBookService.findOneByUser(user.get(), id);
        return ResponseUtil.wrapOrNotFound(splitBookDTO);
    }

    @DeleteMapping("/user/split-books/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteSplitBookByCurrentUser(@PathVariable Long id) {
        log.debug("REST request to delete SplitBook By Current User: {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        splitBookService.deleteByUser(user.get(), id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/split-books/{id}/shared")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<String> getSharedKeyByCurrentUser(@PathVariable Long id) {
        log.debug("REST request to get SplitBook By Current User: {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<SplitBookDTO> splitBookDTO = splitBookService.findOneByUser(user.get(), id);
        if (splitBookDTO.isEmpty()) {
            throw new RuntimeException("No split book was found");
        }

        return ResponseEntity.ok().body(StringUtils.encodeSplitBookKey(user.get().getId(), splitBookDTO.get().getId()));
    }
}
