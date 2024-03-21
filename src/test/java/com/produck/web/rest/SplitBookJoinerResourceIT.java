package com.produck.web.rest;

import static com.produck.domain.SplitBookJoinerAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.SplitBookJoiner;
import com.produck.repository.SplitBookJoinerRepository;
import com.produck.service.dto.SplitBookJoinerDTO;
import com.produck.service.mapper.SplitBookJoinerMapper;
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
 * Integration tests for the {@link SplitBookJoinerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SplitBookJoinerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/split-book-joiners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SplitBookJoinerRepository splitBookJoinerRepository;

    @Autowired
    private SplitBookJoinerMapper splitBookJoinerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSplitBookJoinerMockMvc;

    private SplitBookJoiner splitBookJoiner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBookJoiner createEntity(EntityManager em) {
        SplitBookJoiner splitBookJoiner = new SplitBookJoiner().name(DEFAULT_NAME);
        return splitBookJoiner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBookJoiner createUpdatedEntity(EntityManager em) {
        SplitBookJoiner splitBookJoiner = new SplitBookJoiner().name(UPDATED_NAME);
        return splitBookJoiner;
    }

    @BeforeEach
    public void initTest() {
        splitBookJoiner = createEntity(em);
    }

    @Test
    @Transactional
    void createSplitBookJoiner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);
        var returnedSplitBookJoinerDTO = om.readValue(
            restSplitBookJoinerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookJoinerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SplitBookJoinerDTO.class
        );

        // Validate the SplitBookJoiner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSplitBookJoiner = splitBookJoinerMapper.toEntity(returnedSplitBookJoinerDTO);
        assertSplitBookJoinerUpdatableFieldsEquals(returnedSplitBookJoiner, getPersistedSplitBookJoiner(returnedSplitBookJoiner));
    }

    @Test
    @Transactional
    void createSplitBookJoinerWithExistingId() throws Exception {
        // Create the SplitBookJoiner with an existing ID
        splitBookJoiner.setId(1L);
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSplitBookJoinerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookJoinerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSplitBookJoiners() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        // Get all the splitBookJoinerList
        restSplitBookJoinerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(splitBookJoiner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSplitBookJoiner() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        // Get the splitBookJoiner
        restSplitBookJoinerMockMvc
            .perform(get(ENTITY_API_URL_ID, splitBookJoiner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(splitBookJoiner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSplitBookJoiner() throws Exception {
        // Get the splitBookJoiner
        restSplitBookJoinerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSplitBookJoiner() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookJoiner
        SplitBookJoiner updatedSplitBookJoiner = splitBookJoinerRepository.findById(splitBookJoiner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSplitBookJoiner are not directly saved in db
        em.detach(updatedSplitBookJoiner);
        updatedSplitBookJoiner.name(UPDATED_NAME);
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(updatedSplitBookJoiner);

        restSplitBookJoinerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookJoinerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookJoinerDTO))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSplitBookJoinerToMatchAllProperties(updatedSplitBookJoiner);
    }

    @Test
    @Transactional
    void putNonExistingSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookJoinerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookJoinerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookJoinerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookJoinerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSplitBookJoinerWithPatch() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookJoiner using partial update
        SplitBookJoiner partialUpdatedSplitBookJoiner = new SplitBookJoiner();
        partialUpdatedSplitBookJoiner.setId(splitBookJoiner.getId());

        partialUpdatedSplitBookJoiner.name(UPDATED_NAME);

        restSplitBookJoinerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBookJoiner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBookJoiner))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookJoiner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookJoinerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSplitBookJoiner, splitBookJoiner),
            getPersistedSplitBookJoiner(splitBookJoiner)
        );
    }

    @Test
    @Transactional
    void fullUpdateSplitBookJoinerWithPatch() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookJoiner using partial update
        SplitBookJoiner partialUpdatedSplitBookJoiner = new SplitBookJoiner();
        partialUpdatedSplitBookJoiner.setId(splitBookJoiner.getId());

        partialUpdatedSplitBookJoiner.name(UPDATED_NAME);

        restSplitBookJoinerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBookJoiner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBookJoiner))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookJoiner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookJoinerUpdatableFieldsEquals(
            partialUpdatedSplitBookJoiner,
            getPersistedSplitBookJoiner(partialUpdatedSplitBookJoiner)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, splitBookJoinerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookJoinerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookJoinerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSplitBookJoiner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookJoiner.setId(longCount.incrementAndGet());

        // Create the SplitBookJoiner
        SplitBookJoinerDTO splitBookJoinerDTO = splitBookJoinerMapper.toDto(splitBookJoiner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookJoinerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(splitBookJoinerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBookJoiner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSplitBookJoiner() throws Exception {
        // Initialize the database
        splitBookJoinerRepository.saveAndFlush(splitBookJoiner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the splitBookJoiner
        restSplitBookJoinerMockMvc
            .perform(delete(ENTITY_API_URL_ID, splitBookJoiner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return splitBookJoinerRepository.count();
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

    protected SplitBookJoiner getPersistedSplitBookJoiner(SplitBookJoiner splitBookJoiner) {
        return splitBookJoinerRepository.findById(splitBookJoiner.getId()).orElseThrow();
    }

    protected void assertPersistedSplitBookJoinerToMatchAllProperties(SplitBookJoiner expectedSplitBookJoiner) {
        assertSplitBookJoinerAllPropertiesEquals(expectedSplitBookJoiner, getPersistedSplitBookJoiner(expectedSplitBookJoiner));
    }

    protected void assertPersistedSplitBookJoinerToMatchUpdatableProperties(SplitBookJoiner expectedSplitBookJoiner) {
        assertSplitBookJoinerAllUpdatablePropertiesEquals(expectedSplitBookJoiner, getPersistedSplitBookJoiner(expectedSplitBookJoiner));
    }
}
