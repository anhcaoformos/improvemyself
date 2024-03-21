package com.produck.web.rest;

import com.produck.repository.SplitBookDetailRepository;
import com.produck.service.SplitBookDetailService;
import com.produck.service.dto.SplitBookDetailDTO;
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
 * REST controller for managing {@link com.produck.domain.SplitBookDetail}.
 */
@RestController
@RequestMapping("/api/split-book-details")
public class SplitBookDetailResource {

    private final Logger log = LoggerFactory.getLogger(SplitBookDetailResource.class);

    private static final String ENTITY_NAME = "splitBookDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplitBookDetailService splitBookDetailService;

    private final SplitBookDetailRepository splitBookDetailRepository;

    public SplitBookDetailResource(SplitBookDetailService splitBookDetailService, SplitBookDetailRepository splitBookDetailRepository) {
        this.splitBookDetailService = splitBookDetailService;
        this.splitBookDetailRepository = splitBookDetailRepository;
    }

    /**
     * {@code POST  /split-book-details} : Create a new splitBookDetail.
     *
     * @param splitBookDetailDTO the splitBookDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splitBookDetailDTO, or with status {@code 400 (Bad Request)} if the splitBookDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SplitBookDetailDTO> createSplitBookDetail(@RequestBody SplitBookDetailDTO splitBookDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save SplitBookDetail : {}", splitBookDetailDTO);
        if (splitBookDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBookDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splitBookDetailDTO = splitBookDetailService.save(splitBookDetailDTO);
        return ResponseEntity.created(new URI("/api/split-book-details/" + splitBookDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, splitBookDetailDTO.getId().toString()))
            .body(splitBookDetailDTO);
    }

    /**
     * {@code PUT  /split-book-details/:id} : Updates an existing splitBookDetail.
     *
     * @param id the id of the splitBookDetailDTO to save.
     * @param splitBookDetailDTO the splitBookDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookDetailDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the splitBookDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SplitBookDetailDTO> updateSplitBookDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDetailDTO splitBookDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SplitBookDetail : {}, {}", id, splitBookDetailDTO);
        if (splitBookDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        splitBookDetailDTO = splitBookDetailService.update(splitBookDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDetailDTO.getId().toString()))
            .body(splitBookDetailDTO);
    }

    /**
     * {@code PATCH  /split-book-details/:id} : Partial updates given fields of an existing splitBookDetail, field will ignore if it is null
     *
     * @param id the id of the splitBookDetailDTO to save.
     * @param splitBookDetailDTO the splitBookDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookDetailDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the splitBookDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the splitBookDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SplitBookDetailDTO> partialUpdateSplitBookDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDetailDTO splitBookDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SplitBookDetail partially : {}, {}", id, splitBookDetailDTO);
        if (splitBookDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplitBookDetailDTO> result = splitBookDetailService.partialUpdate(splitBookDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /split-book-details} : get all the splitBookDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of splitBookDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SplitBookDetailDTO>> getAllSplitBookDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SplitBookDetails");
        Page<SplitBookDetailDTO> page = splitBookDetailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /split-book-details/:id} : get the "id" splitBookDetail.
     *
     * @param id the id of the splitBookDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the splitBookDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SplitBookDetailDTO> getSplitBookDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get SplitBookDetail : {}", id);
        Optional<SplitBookDetailDTO> splitBookDetailDTO = splitBookDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(splitBookDetailDTO);
    }

    /**
     * {@code DELETE  /split-book-details/:id} : delete the "id" splitBookDetail.
     *
     * @param id the id of the splitBookDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSplitBookDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete SplitBookDetail : {}", id);
        splitBookDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
