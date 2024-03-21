package com.produck.web.rest;

import static com.produck.domain.LedgerAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.Ledger;
import com.produck.repository.LedgerRepository;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.mapper.LedgerMapper;
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
 * Integration tests for the {@link LedgerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LedgerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String ENTITY_API_URL = "/api/ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LedgerMapper ledgerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLedgerMockMvc;

    private Ledger ledger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ledger createEntity(EntityManager em) {
        Ledger ledger = new Ledger().name(DEFAULT_NAME).isDefault(DEFAULT_IS_DEFAULT);
        return ledger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ledger createUpdatedEntity(EntityManager em) {
        Ledger ledger = new Ledger().name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
        return ledger;
    }

    @BeforeEach
    public void initTest() {
        ledger = createEntity(em);
    }

    @Test
    @Transactional
    void createLedger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);
        var returnedLedgerDTO = om.readValue(
            restLedgerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LedgerDTO.class
        );

        // Validate the Ledger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLedger = ledgerMapper.toEntity(returnedLedgerDTO);
        assertLedgerUpdatableFieldsEquals(returnedLedger, getPersistedLedger(returnedLedger));
    }

    @Test
    @Transactional
    void createLedgerWithExistingId() throws Exception {
        // Create the Ledger with an existing ID
        ledger.setId(1L);
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLedgers() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get all the ledgerList
        restLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    void getLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get the ledger
        restLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, ledger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLedger() throws Exception {
        // Get the ledger
        restLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledger
        Ledger updatedLedger = ledgerRepository.findById(ledger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLedger are not directly saved in db
        em.detach(updatedLedger);
        updatedLedger.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);
        LedgerDTO ledgerDTO = ledgerMapper.toDto(updatedLedger);

        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLedgerToMatchAllProperties(updatedLedger);
    }

    @Test
    @Transactional
    void putNonExistingLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ledgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLedgerWithPatch() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledger using partial update
        Ledger partialUpdatedLedger = new Ledger();
        partialUpdatedLedger.setId(ledger.getId());

        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLedger))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLedgerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLedger, ledger), getPersistedLedger(ledger));
    }

    @Test
    @Transactional
    void fullUpdateLedgerWithPatch() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledger using partial update
        Ledger partialUpdatedLedger = new Ledger();
        partialUpdatedLedger.setId(ledger.getId());

        partialUpdatedLedger.name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT);

        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLedger))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLedgerUpdatableFieldsEquals(partialUpdatedLedger, getPersistedLedger(partialUpdatedLedger));
    }

    @Test
    @Transactional
    void patchNonExistingLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ledgerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ledgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ledgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledger.setId(longCount.incrementAndGet());

        // Create the Ledger
        LedgerDTO ledgerDTO = ledgerMapper.toDto(ledger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ledgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ledger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ledger
        restLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ledger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ledgerRepository.count();
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

    protected Ledger getPersistedLedger(Ledger ledger) {
        return ledgerRepository.findById(ledger.getId()).orElseThrow();
    }

    protected void assertPersistedLedgerToMatchAllProperties(Ledger expectedLedger) {
        assertLedgerAllPropertiesEquals(expectedLedger, getPersistedLedger(expectedLedger));
    }

    protected void assertPersistedLedgerToMatchUpdatableProperties(Ledger expectedLedger) {
        assertLedgerAllUpdatablePropertiesEquals(expectedLedger, getPersistedLedger(expectedLedger));
    }
}
