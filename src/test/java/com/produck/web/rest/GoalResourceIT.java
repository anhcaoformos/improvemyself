package com.produck.web.rest;

import static com.produck.domain.GoalAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.Goal;
import com.produck.domain.enumeration.Priority;
import com.produck.repository.GoalRepository;
import com.produck.service.dto.GoalDTO;
import com.produck.service.mapper.GoalMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GoalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoalResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Priority DEFAULT_PRIORITY = Priority.HIGH;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final String ENTITY_API_URL = "/api/goals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalMapper goalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGoalMockMvc;

    private Goal goal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createEntity(EntityManager em) {
        Goal goal = new Goal().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).priority(DEFAULT_PRIORITY);
        return goal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createUpdatedEntity(EntityManager em) {
        Goal goal = new Goal().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);
        return goal;
    }

    @BeforeEach
    public void initTest() {
        goal = createEntity(em);
    }

    @Test
    @Transactional
    void createGoal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);
        var returnedGoalDTO = om.readValue(
            restGoalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GoalDTO.class
        );

        // Validate the Goal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGoal = goalMapper.toEntity(returnedGoalDTO);
        assertGoalUpdatableFieldsEquals(returnedGoal, getPersistedGoal(returnedGoal));
    }

    @Test
    @Transactional
    void createGoalWithExistingId() throws Exception {
        // Create the Goal with an existing ID
        goal.setId(1L);
        GoalDTO goalDTO = goalMapper.toDto(goal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGoals() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goalList
        restGoalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())));
    }

    @Test
    @Transactional
    void getGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get the goal
        restGoalMockMvc
            .perform(get(ENTITY_API_URL_ID, goal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(goal.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGoal() throws Exception {
        // Get the goal
        restGoalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goal
        Goal updatedGoal = goalRepository.findById(goal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGoal are not directly saved in db
        em.detach(updatedGoal);
        updatedGoal.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);
        GoalDTO goalDTO = goalMapper.toDto(updatedGoal);

        restGoalMockMvc
            .perform(put(ENTITY_API_URL_ID, goalDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goalDTO)))
            .andExpect(status().isOk());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGoalToMatchAllProperties(updatedGoal);
    }

    @Test
    @Transactional
    void putNonExistingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(put(ENTITY_API_URL_ID, goalDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(goalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(goalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        partialUpdatedGoal.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGoalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGoal, goal), getPersistedGoal(goal));
    }

    @Test
    @Transactional
    void fullUpdateGoalWithPatch() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the goal using partial update
        Goal partialUpdatedGoal = new Goal();
        partialUpdatedGoal.setId(goal.getId());

        partialUpdatedGoal.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).priority(UPDATED_PRIORITY);

        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGoal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGoal))
            )
            .andExpect(status().isOk());

        // Validate the Goal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGoalUpdatableFieldsEquals(partialUpdatedGoal, getPersistedGoal(partialUpdatedGoal));
    }

    @Test
    @Transactional
    void patchNonExistingGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, goalDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(goalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(goalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGoal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        goal.setId(longCount.incrementAndGet());

        // Create the Goal
        GoalDTO goalDTO = goalMapper.toDto(goal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGoalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(goalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Goal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the goal
        restGoalMockMvc
            .perform(delete(ENTITY_API_URL_ID, goal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return goalRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Goal getPersistedGoal(Goal goal) {
        return goalRepository.findById(goal.getId()).orElseThrow();
    }

    protected void assertPersistedGoalToMatchAllProperties(Goal expectedGoal) {
        assertGoalAllPropertiesEquals(expectedGoal, getPersistedGoal(expectedGoal));
    }

    protected void assertPersistedGoalToMatchUpdatableProperties(Goal expectedGoal) {
        assertGoalAllUpdatablePropertiesEquals(expectedGoal, getPersistedGoal(expectedGoal));
    }
}
