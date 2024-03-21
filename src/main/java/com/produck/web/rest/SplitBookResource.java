package com.produck.web.rest;

import com.produck.repository.SplitBookRepository;
import com.produck.service.SplitBookService;
import com.produck.service.dto.SplitBookDTO;
import com.produck.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    public SplitBookResource(SplitBookService splitBookService, SplitBookRepository splitBookRepository) {
        this.splitBookService = splitBookService;
        this.splitBookRepository = splitBookRepository;
    }

    /**
     * {@code POST  /split-books} : Create a new splitBook.
     *
     * @param splitBookDTO the splitBookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splitBookDTO, or with status {@code 400 (Bad Request)} if the splitBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SplitBookDTO> createSplitBook(@RequestBody SplitBookDTO splitBookDTO) throws URISyntaxException {
        log.debug("REST request to save SplitBook : {}", splitBookDTO);
        if (splitBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splitBookDTO = splitBookService.save(splitBookDTO);
        return ResponseEntity.created(new URI("/api/split-books/" + splitBookDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString()))
            .body(splitBookDTO);
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
    @PutMapping("/{id}")
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

        splitBookDTO = splitBookService.update(splitBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDTO.getId().toString()))
            .body(splitBookDTO);
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
    @GetMapping("")
    public ResponseEntity<List<SplitBookDTO>> getAllSplitBooks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
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
    @GetMapping("/{id}")
    public ResponseEntity<SplitBookDTO> getSplitBook(@PathVariable("id") Long id) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSplitBook(@PathVariable("id") Long id) {
        log.debug("REST request to delete SplitBook : {}", id);
        splitBookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
