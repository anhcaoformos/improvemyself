package com.produck.web.rest;

import com.produck.repository.GoalRepository;
import com.produck.service.GoalService;
import com.produck.service.dto.GoalDTO;
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
 * REST controller for managing {@link com.produck.domain.Goal}.
 */
@RestController
@RequestMapping("/api/goals")
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);

    private static final String ENTITY_NAME = "goal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoalService goalService;

    private final GoalRepository goalRepository;

    public GoalResource(GoalService goalService, GoalRepository goalRepository) {
        this.goalService = goalService;
        this.goalRepository = goalRepository;
    }

    /**
     * {@code POST  /goals} : Create a new goal.
     *
     * @param goalDTO the goalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goalDTO, or with status {@code 400 (Bad Request)} if the goal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goalDTO);
        if (goalDTO.getId() != null) {
            throw new BadRequestAlertException("A new goal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        goalDTO = goalService.save(goalDTO);
        return ResponseEntity.created(new URI("/api/goals/" + goalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(goalDTO);
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
    @PutMapping("/{id}")
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

        goalDTO = goalService.update(goalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goalDTO.getId().toString()))
            .body(goalDTO);
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
    @GetMapping("")
    public ResponseEntity<List<GoalDTO>> getAllGoals(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
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
    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> getGoal(@PathVariable("id") Long id) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable("id") Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
