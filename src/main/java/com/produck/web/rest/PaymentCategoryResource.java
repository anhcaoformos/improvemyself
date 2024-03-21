package com.produck.web.rest;

import com.produck.repository.PaymentCategoryRepository;
import com.produck.service.PaymentCategoryService;
import com.produck.service.dto.PaymentCategoryDTO;
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

    public PaymentCategoryResource(PaymentCategoryService paymentCategoryService, PaymentCategoryRepository paymentCategoryRepository) {
        this.paymentCategoryService = paymentCategoryService;
        this.paymentCategoryRepository = paymentCategoryRepository;
    }

    /**
     * {@code POST  /payment-categories} : Create a new paymentCategory.
     *
     * @param paymentCategoryDTO the paymentCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCategoryDTO, or with status {@code 400 (Bad Request)} if the paymentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentCategoryDTO> createPaymentCategory(@RequestBody PaymentCategoryDTO paymentCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentCategory : {}", paymentCategoryDTO);
        if (paymentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentCategoryDTO = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity.created(new URI("/api/payment-categories/" + paymentCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(paymentCategoryDTO);
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
    @PutMapping("/{id}")
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

        paymentCategoryDTO = paymentCategoryService.update(paymentCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(paymentCategoryDTO);
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllPaymentCategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("transaction-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentCategorys where transaction is null");
            return new ResponseEntity<>(paymentCategoryService.findAllWhereTransactionIsNull(), HttpStatus.OK);
        }

        if ("objective-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentCategorys where objective is null");
            return new ResponseEntity<>(paymentCategoryService.findAllWhereObjectiveIsNull(), HttpStatus.OK);
        }
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
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCategoryDTO> getPaymentCategory(@PathVariable("id") Long id) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaymentCategory : {}", id);
        paymentCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
