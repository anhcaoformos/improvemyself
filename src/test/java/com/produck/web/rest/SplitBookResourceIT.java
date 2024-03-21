package com.produck.web.rest;

import static com.produck.domain.SplitBookAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.SplitBook;
import com.produck.repository.SplitBookRepository;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.mapper.SplitBookMapper;
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
 * Integration tests for the {@link SplitBookResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SplitBookResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/split-books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SplitBookRepository splitBookRepository;

    @Autowired
    private SplitBookMapper splitBookMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSplitBookMockMvc;

    private SplitBook splitBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBook createEntity(EntityManager em) {
        SplitBook splitBook = new SplitBook().description(DEFAULT_DESCRIPTION).name(DEFAULT_NAME);
        return splitBook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBook createUpdatedEntity(EntityManager em) {
        SplitBook splitBook = new SplitBook().description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        return splitBook;
    }

    @BeforeEach
    public void initTest() {
        splitBook = createEntity(em);
    }

    @Test
    @Transactional
    void createSplitBook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);
        var returnedSplitBookDTO = om.readValue(
            restSplitBookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SplitBookDTO.class
        );

        // Validate the SplitBook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSplitBook = splitBookMapper.toEntity(returnedSplitBookDTO);
        assertSplitBookUpdatableFieldsEquals(returnedSplitBook, getPersistedSplitBook(returnedSplitBook));
    }

    @Test
    @Transactional
    void createSplitBookWithExistingId() throws Exception {
        // Create the SplitBook with an existing ID
        splitBook.setId(1L);
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSplitBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSplitBooks() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        // Get all the splitBookList
        restSplitBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(splitBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSplitBook() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        // Get the splitBook
        restSplitBookMockMvc
            .perform(get(ENTITY_API_URL_ID, splitBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(splitBook.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSplitBook() throws Exception {
        // Get the splitBook
        restSplitBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSplitBook() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBook
        SplitBook updatedSplitBook = splitBookRepository.findById(splitBook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSplitBook are not directly saved in db
        em.detach(updatedSplitBook);
        updatedSplitBook.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(updatedSplitBook);

        restSplitBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDTO))
            )
            .andExpect(status().isOk());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSplitBookToMatchAllProperties(updatedSplitBook);
    }

    @Test
    @Transactional
    void putNonExistingSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSplitBookWithPatch() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBook using partial update
        SplitBook partialUpdatedSplitBook = new SplitBook();
        partialUpdatedSplitBook.setId(splitBook.getId());

        partialUpdatedSplitBook.name(UPDATED_NAME);

        restSplitBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBook))
            )
            .andExpect(status().isOk());

        // Validate the SplitBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSplitBook, splitBook),
            getPersistedSplitBook(splitBook)
        );
    }

    @Test
    @Transactional
    void fullUpdateSplitBookWithPatch() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBook using partial update
        SplitBook partialUpdatedSplitBook = new SplitBook();
        partialUpdatedSplitBook.setId(splitBook.getId());

        partialUpdatedSplitBook.description(UPDATED_DESCRIPTION).name(UPDATED_NAME);

        restSplitBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBook))
            )
            .andExpect(status().isOk());

        // Validate the SplitBook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookUpdatableFieldsEquals(partialUpdatedSplitBook, getPersistedSplitBook(partialUpdatedSplitBook));
    }

    @Test
    @Transactional
    void patchNonExistingSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, splitBookDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSplitBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBook.setId(longCount.incrementAndGet());

        // Create the SplitBook
        SplitBookDTO splitBookDTO = splitBookMapper.toDto(splitBook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(splitBookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSplitBook() throws Exception {
        // Initialize the database
        splitBookRepository.saveAndFlush(splitBook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the splitBook
        restSplitBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, splitBook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return splitBookRepository.count();
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

    protected SplitBook getPersistedSplitBook(SplitBook splitBook) {
        return splitBookRepository.findById(splitBook.getId()).orElseThrow();
    }

    protected void assertPersistedSplitBookToMatchAllProperties(SplitBook expectedSplitBook) {
        assertSplitBookAllPropertiesEquals(expectedSplitBook, getPersistedSplitBook(expectedSplitBook));
    }

    protected void assertPersistedSplitBookToMatchUpdatableProperties(SplitBook expectedSplitBook) {
        assertSplitBookAllUpdatablePropertiesEquals(expectedSplitBook, getPersistedSplitBook(expectedSplitBook));
    }
}
