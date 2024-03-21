package com.produck.web.rest;

import static com.produck.domain.ObjectiveAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.Objective;
import com.produck.domain.enumeration.ObjectiveType;
import com.produck.repository.ObjectiveRepository;
import com.produck.service.dto.ObjectiveDTO;
import com.produck.service.mapper.ObjectiveMapper;
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
 * Integration tests for the {@link ObjectiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjectiveResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ObjectiveType DEFAULT_TYPE = ObjectiveType.FROM_OBJECTIVE;
    private static final ObjectiveType UPDATED_TYPE = ObjectiveType.TO_OBJECTIVE;

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    private static final String ENTITY_API_URL = "/api/objectives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private ObjectiveMapper objectiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjectiveMockMvc;

    private Objective objective;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objective createEntity(EntityManager em) {
        Objective objective = new Objective().name(DEFAULT_NAME).type(DEFAULT_TYPE).isHidden(DEFAULT_IS_HIDDEN);
        return objective;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objective createUpdatedEntity(EntityManager em) {
        Objective objective = new Objective().name(UPDATED_NAME).type(UPDATED_TYPE).isHidden(UPDATED_IS_HIDDEN);
        return objective;
    }

    @BeforeEach
    public void initTest() {
        objective = createEntity(em);
    }

    @Test
    @Transactional
    void createObjective() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);
        var returnedObjectiveDTO = om.readValue(
            restObjectiveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(objectiveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ObjectiveDTO.class
        );

        // Validate the Objective in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedObjective = objectiveMapper.toEntity(returnedObjectiveDTO);
        assertObjectiveUpdatableFieldsEquals(returnedObjective, getPersistedObjective(returnedObjective));
    }

    @Test
    @Transactional
    void createObjectiveWithExistingId() throws Exception {
        // Create the Objective with an existing ID
        objective.setId(1L);
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjectiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(objectiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObjectives() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        // Get all the objectiveList
        restObjectiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objective.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    void getObjective() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        // Get the objective
        restObjectiveMockMvc
            .perform(get(ENTITY_API_URL_ID, objective.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objective.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingObjective() throws Exception {
        // Get the objective
        restObjectiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingObjective() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objective
        Objective updatedObjective = objectiveRepository.findById(objective.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedObjective are not directly saved in db
        em.detach(updatedObjective);
        updatedObjective.name(UPDATED_NAME).type(UPDATED_TYPE).isHidden(UPDATED_IS_HIDDEN);
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(updatedObjective);

        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objectiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(objectiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedObjectiveToMatchAllProperties(updatedObjective);
    }

    @Test
    @Transactional
    void putNonExistingObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objectiveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(objectiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(objectiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(objectiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjectiveWithPatch() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objective using partial update
        Objective partialUpdatedObjective = new Objective();
        partialUpdatedObjective.setId(objective.getId());

        partialUpdatedObjective.type(UPDATED_TYPE).isHidden(UPDATED_IS_HIDDEN);

        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjective.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObjective))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObjectiveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedObjective, objective),
            getPersistedObjective(objective)
        );
    }

    @Test
    @Transactional
    void fullUpdateObjectiveWithPatch() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the objective using partial update
        Objective partialUpdatedObjective = new Objective();
        partialUpdatedObjective.setId(objective.getId());

        partialUpdatedObjective.name(UPDATED_NAME).type(UPDATED_TYPE).isHidden(UPDATED_IS_HIDDEN);

        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjective.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObjective))
            )
            .andExpect(status().isOk());

        // Validate the Objective in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObjectiveUpdatableFieldsEquals(partialUpdatedObjective, getPersistedObjective(partialUpdatedObjective));
    }

    @Test
    @Transactional
    void patchNonExistingObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objectiveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(objectiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(objectiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjective() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        objective.setId(longCount.incrementAndGet());

        // Create the Objective
        ObjectiveDTO objectiveDTO = objectiveMapper.toDto(objective);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(objectiveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objective in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjective() throws Exception {
        // Initialize the database
        objectiveRepository.saveAndFlush(objective);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the objective
        restObjectiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, objective.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return objectiveRepository.count();
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

    protected Objective getPersistedObjective(Objective objective) {
        return objectiveRepository.findById(objective.getId()).orElseThrow();
    }

    protected void assertPersistedObjectiveToMatchAllProperties(Objective expectedObjective) {
        assertObjectiveAllPropertiesEquals(expectedObjective, getPersistedObjective(expectedObjective));
    }

    protected void assertPersistedObjectiveToMatchUpdatableProperties(Objective expectedObjective) {
        assertObjectiveAllUpdatablePropertiesEquals(expectedObjective, getPersistedObjective(expectedObjective));
    }
}
