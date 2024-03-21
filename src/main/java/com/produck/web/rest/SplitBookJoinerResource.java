package com.produck.web.rest;

import com.produck.repository.SplitBookJoinerRepository;
import com.produck.service.SplitBookJoinerService;
import com.produck.service.dto.SplitBookJoinerDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.produck.domain.SplitBookJoiner}.
 */
@RestController
@RequestMapping("/api/split-book-joiners")
public class SplitBookJoinerResource {

    private final Logger log = LoggerFactory.getLogger(SplitBookJoinerResource.class);

    private static final String ENTITY_NAME = "splitBookJoiner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplitBookJoinerService splitBookJoinerService;

    private final SplitBookJoinerRepository splitBookJoinerRepository;

    public SplitBookJoinerResource(SplitBookJoinerService splitBookJoinerService, SplitBookJoinerRepository splitBookJoinerRepository) {
        this.splitBookJoinerService = splitBookJoinerService;
        this.splitBookJoinerRepository = splitBookJoinerRepository;
    }

    /**
     * {@code POST  /split-book-joiners} : Create a new splitBookJoiner.
     *
     * @param splitBookJoinerDTO the splitBookJoinerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splitBookJoinerDTO, or with status {@code 400 (Bad Request)} if the splitBookJoiner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SplitBookJoinerDTO> createSplitBookJoiner(@RequestBody SplitBookJoinerDTO splitBookJoinerDTO)
        throws URISyntaxException {
        log.debug("REST request to save SplitBookJoiner : {}", splitBookJoinerDTO);
        if (splitBookJoinerDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBookJoiner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splitBookJoinerDTO = splitBookJoinerService.save(splitBookJoinerDTO);
        return ResponseEntity.created(new URI("/api/split-book-joiners/" + splitBookJoinerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, splitBookJoinerDTO.getId().toString()))
            .body(splitBookJoinerDTO);
    }

    /**
     * {@code PUT  /split-book-joiners/:id} : Updates an existing splitBookJoiner.
     *
     * @param id the id of the splitBookJoinerDTO to save.
     * @param splitBookJoinerDTO the splitBookJoinerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookJoinerDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookJoinerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the splitBookJoinerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SplitBookJoinerDTO> updateSplitBookJoiner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookJoinerDTO splitBookJoinerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SplitBookJoiner : {}, {}", id, splitBookJoinerDTO);
        if (splitBookJoinerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookJoinerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookJoinerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        splitBookJoinerDTO = splitBookJoinerService.update(splitBookJoinerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookJoinerDTO.getId().toString()))
            .body(splitBookJoinerDTO);
    }

    /**
     * {@code PATCH  /split-book-joiners/:id} : Partial updates given fields of an existing splitBookJoiner, field will ignore if it is null
     *
     * @param id the id of the splitBookJoinerDTO to save.
     * @param splitBookJoinerDTO the splitBookJoinerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splitBookJoinerDTO,
     * or with status {@code 400 (Bad Request)} if the splitBookJoinerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the splitBookJoinerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the splitBookJoinerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SplitBookJoinerDTO> partialUpdateSplitBookJoiner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookJoinerDTO splitBookJoinerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SplitBookJoiner partially : {}, {}", id, splitBookJoinerDTO);
        if (splitBookJoinerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookJoinerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookJoinerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplitBookJoinerDTO> result = splitBookJoinerService.partialUpdate(splitBookJoinerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookJoinerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /split-book-joiners} : get all the splitBookJoiners.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of splitBookJoiners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SplitBookJoinerDTO>> getAllSplitBookJoiners(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("splitbookdetail-is-null".equals(filter)) {
            log.debug("REST request to get all SplitBookJoiners where splitBookDetail is null");
            return new ResponseEntity<>(splitBookJoinerService.findAllWhereSplitBookDetailIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of SplitBookJoiners");
        Page<SplitBookJoinerDTO> page = splitBookJoinerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /split-book-joiners/:id} : get the "id" splitBookJoiner.
     *
     * @param id the id of the splitBookJoinerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the splitBookJoinerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SplitBookJoinerDTO> getSplitBookJoiner(@PathVariable("id") Long id) {
        log.debug("REST request to get SplitBookJoiner : {}", id);
        Optional<SplitBookJoinerDTO> splitBookJoinerDTO = splitBookJoinerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(splitBookJoinerDTO);
    }

    /**
     * {@code DELETE  /split-book-joiners/:id} : delete the "id" splitBookJoiner.
     *
     * @param id the id of the splitBookJoinerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSplitBookJoiner(@PathVariable("id") Long id) {
        log.debug("REST request to delete SplitBookJoiner : {}", id);
        splitBookJoinerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
