package com.produck.web.rest;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.repository.GoalRepository;
import com.produck.repository.LedgerRepository;
import com.produck.security.AuthoritiesConstants;
import com.produck.service.GoalService;
import com.produck.service.UserService;
import com.produck.service.dto.GoalDTO;
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
 * REST controller for managing {@link com.produck.domain.Goal}.
 */
@RestController
@RequestMapping("/api")
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);

    private static final String ENTITY_NAME = "goal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoalService goalService;

    private final GoalRepository goalRepository;

    private final UserService userService;

    private final LedgerRepository ledgerRepository;

    public GoalResource(
        GoalService goalService,
        GoalRepository goalRepository,
        UserService userService,
        LedgerRepository ledgerRepository
    ) {
        this.goalService = goalService;
        this.goalRepository = goalRepository;
        this.userService = userService;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * {@code POST  /goals} : Create a new goal.
     *
     * @param goalDTO the goalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goalDTO, or with status {@code 400 (Bad Request)} if the goal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goals")
    public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goals/:id} : Updates an existing goal.
     *
     * @param id the id of the goalDTO to save.
     * @param goalDTO the goalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goalDTO,
     * or with status {@code 400 (Bad Request)} if the goalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goals/{id}")
    public ResponseEntity<GoalDTO> updateGoal(@PathVariable(value = "id", required = false) final Long id, @RequestBody GoalDTO goalDTO)
        throws URISyntaxException {
        log.debug("REST request to update Goal : {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoalDTO result = goalService.update(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /goals/:id} : Partial updates given fields of an existing goal, field will ignore if it is null
     *
     * @param id the id of the goalDTO to save.
     * @param goalDTO the goalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goalDTO,
     * or with status {@code 400 (Bad Request)} if the goalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the goalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the goalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/goals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GoalDTO> partialUpdateGoal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GoalDTO goalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Goal partially : {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoalDTO> result = goalService.partialUpdate(goalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /goals} : get all the goals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goals in body.
     */
    @GetMapping("/goals")
    public ResponseEntity<List<GoalDTO>> getAllGoals(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Goals");
        Page<GoalDTO> page = goalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /goals/:id} : get the "id" goal.
     *
     * @param id the id of the goalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalDTO> getGoal(@PathVariable Long id) {
        log.debug("REST request to get Goal : {}", id);
        Optional<GoalDTO> goalDTO = goalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goalDTO);
    }

    /**
     * {@code DELETE  /goals/:id} : delete the "id" goal.
     *
     * @param id the id of the goalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/goals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<GoalDTO>> getAllPaymentCategoriesByDefaultLedger() {
        log.debug("REST request to get a page of Payment Categories By Default Ledger");
        Ledger ledger = validateAndGetDefaultLedger();
        return ResponseEntity.ok().body(goalService.findAllByLedger(ledger));
    }

    @PostMapping("/user/ledger/goals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> createGoalByDefaultLedger(@RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to save Goal By Default Ledger: {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new Common Target cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ledger ledger = validateAndGetDefaultLedger();
        // TODO check unique
        //        if (Boolean.TRUE.equals(goalRepository.existsByLedgerAndCode(ledger, goalDTO.getPaymentCategory().getCode()))) {
        //            throw new BadRequestAlertException("A new Common Target cannot already have an Code", ENTITY_NAME, "codeexists");
        //        }
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> updateGoalByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GoalDTO goalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Goal By Default Ledger: {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoalDTO result = goalService.update(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/goals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> partialUpdateGoalByDefaultLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GoalDTO goalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Goal partially By Default Ledger: {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetDefaultLedger();
        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoalDTO> result = goalService.partialUpdate(goalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> getGoalByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to get Goal By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        Optional<GoalDTO> goalDTO = goalService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(goalDTO);
    }

    @DeleteMapping("/user/ledger/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteGoalByDefaultLedger(@PathVariable Long id) {
        log.debug("REST request to delete Goal By Default Ledger: {}", id);
        Ledger ledger = validateAndGetDefaultLedger();
        goalService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/user/ledger/{ledgerId}/goals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<GoalDTO>> getAllPaymentCategoriesByLedger(@PathVariable("ledgerId") Long ledgerId) {
        log.debug("REST request to get a page of Payment Categories By Ledger");
        Ledger ledger = validateAndGetLedger(ledgerId);
        return ResponseEntity.ok().body(goalService.findAllByLedger(ledger));
    }

    @PostMapping("/user/ledger/{ledgerId}/goals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> createGoalByLedger(@PathVariable("ledgerId") Long ledgerId, @RequestBody GoalDTO goalDTO)
        throws URISyntaxException {
        log.debug("REST request to save Goal By Ledger: {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validateAndGetLedger(ledgerId);
        GoalDTO result = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/user/ledger/" + ledgerId + "/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/user/ledger/{ledgerId}/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> updateGoalByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GoalDTO goalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Goal By Ledger: {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GoalDTO result = goalService.update(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/user/ledger/{ledgerId}/goals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> partialUpdateGoalByLedger(
        @PathVariable("ledgerId") Long ledgerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GoalDTO goalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Goal partially By Ledger: {}, {}", id, goalDTO);
        if (goalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        validateAndGetLedger(ledgerId);
        if (!goalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GoalDTO> result = goalService.partialUpdate(goalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString())
        );
    }

    @GetMapping("/user/ledger/{ledgerId}/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<GoalDTO> getGoalByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to get Goal By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        Optional<GoalDTO> goalDTO = goalService.findOneByLedger(ledger, id);
        return ResponseUtil.wrapOrNotFound(goalDTO);
    }

    @DeleteMapping("/user/ledger/{ledgerId}/goals/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteGoalByLedger(@PathVariable("ledgerId") Long ledgerId, @PathVariable Long id) {
        log.debug("REST request to delete Goal By Ledger: {}", id);
        Ledger ledger = validateAndGetLedger(ledgerId);
        goalService.deleteByLedger(ledger, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
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
