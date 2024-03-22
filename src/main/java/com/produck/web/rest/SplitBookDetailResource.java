package com.produck.web.rest;

import com.produck.domain.SplitBook;
import com.produck.repository.SplitBookDetailRepository;
import com.produck.repository.SplitBookRepository;
import com.produck.service.SplitBookDetailService;
import com.produck.service.UserService;
import com.produck.service.dto.SplitBookDetailDTO;
import com.produck.service.mapper.SplitBookMapper;
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
import org.springframework.data.util.Pair;
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

    private final SplitBookMapper splitBookMapper;

    private final SplitBookDetailRepository splitBookDetailRepository;

    private final UserService userService;

    private final SplitBookRepository splitBookRepository;

    public SplitBookDetailResource(
        SplitBookDetailService splitBookDetailService,
        SplitBookMapper splitBookMapper,
        SplitBookDetailRepository splitBookDetailRepository,
        SplitBookRepository splitBookRepository,
        UserService userService
    ) {
        this.splitBookDetailService = splitBookDetailService;
        this.splitBookMapper = splitBookMapper;
        this.splitBookDetailRepository = splitBookDetailRepository;
        this.splitBookRepository = splitBookRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /split-book-details} : Create a new splitBookDetail.
     *
     * @param splitBookDetailDTO the splitBookDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splitBookDetailDTO, or with status {@code 400 (Bad Request)} if the splitBookDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/split-book-details")
    public ResponseEntity<SplitBookDetailDTO> createSplitBookDetail(@RequestBody SplitBookDetailDTO splitBookDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save SplitBookDetail : {}", splitBookDetailDTO);
        if (splitBookDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBookDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SplitBookDetailDTO result = splitBookDetailService.save(splitBookDetailDTO);
        return ResponseEntity.created(new URI("/api/split-book-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    @PutMapping("/split-book-details/{id}")
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

        SplitBookDetailDTO result = splitBookDetailService.update(splitBookDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDetailDTO.getId().toString()))
            .body(result);
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
    @PatchMapping(value = "/split-book-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
    @GetMapping("/split-book-details")
    public ResponseEntity<List<SplitBookDetailDTO>> getAllSplitBookDetails(@ParameterObject Pageable pageable) {
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
    @GetMapping("/split-book-details/{id}")
    public ResponseEntity<SplitBookDetailDTO> getSplitBookDetail(@PathVariable Long id) {
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
    @DeleteMapping("/split-book-details/{id}")
    public ResponseEntity<Void> deleteSplitBookDetail(@PathVariable Long id) {
        log.debug("REST request to delete SplitBookDetail : {}", id);
        splitBookDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/split-book-details/shared/{key}")
    public ResponseEntity<List<SplitBookDetailDTO>> getAllSplitBookDetailsShared(
        @PathVariable("key") String key,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SplitBookDetails");
        SplitBook splitBook = validateAndGetSplitBook(StringUtils.decodeSplitBookKey(key));
        Page<SplitBookDetailDTO> page = splitBookDetailService.findAllShared(splitBook, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/user/split-book-details/{id}")
    public ResponseEntity<SplitBookDetailDTO> updateUserSplitBookDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SplitBookDetailDTO splitBookDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SplitBookDetail : {}, {}", id, splitBookDetailDTO);
        SplitBook splitBook = validateAndGetSplitBook(StringUtils.decodeSplitBookKey(splitBookDetailDTO.getSharedKey()));

        if (splitBookDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splitBookDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splitBookDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        splitBookDetailDTO.setSplitBook(splitBookMapper.toDto(splitBook));
        SplitBookDetailDTO result = splitBookDetailService.update(splitBookDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splitBookDetailDTO.getId().toString()))
            .body(result);
    }

    @PostMapping("/user/split-book-details")
    public ResponseEntity<SplitBookDetailDTO> createUserSplitBookDetail(@RequestBody SplitBookDetailDTO splitBookDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save SplitBookDetail : {}", splitBookDetailDTO);
        SplitBook splitBook = validateAndGetSplitBook(StringUtils.decodeSplitBookKey(splitBookDetailDTO.getSharedKey()));
        if (
            Objects.isNull(splitBookDetailDTO.getPersonName()) ||
            Objects.isNull(splitBookDetailDTO.getAmount()) ||
            Objects.isNull(splitBookDetailDTO.getTransactionDate()) ||
            Objects.isNull(splitBookDetailDTO.getTransactionType())
        ) {
            throw new BadRequestAlertException("Bad request", ENTITY_NAME, "badrequest");
        }
        if (splitBookDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new splitBookDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splitBookDetailDTO.setSplitBook(splitBookMapper.toDto(splitBook));

        SplitBookDetailDTO result = splitBookDetailService.save(splitBookDetailDTO);
        return ResponseEntity.created(new URI("/api/split-book-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private SplitBook validateAndGetSplitBook(Pair<Long, Long> userAndSplitBook) {
        Long userId;
        Long splitBookId;
        try {
            userId = userAndSplitBook.getFirst();
            splitBookId = userAndSplitBook.getSecond();
        } catch (Exception exception) {
            throw new BadRequestAlertException("Key are not invalid", ENTITY_NAME, "keynotinvalid");
        }
        if (userService.getUserBy(userId).isEmpty()) {
            throw new RuntimeException("No user was found");
        }
        Optional<SplitBook> splitBookOptional = splitBookRepository.findById(splitBookId);
        if (splitBookOptional.isEmpty()) {
            throw new RuntimeException("No split book was found");
        }
        return splitBookOptional.get();
    }
}
