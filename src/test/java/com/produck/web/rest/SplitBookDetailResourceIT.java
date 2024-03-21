package com.produck.web.rest;

import static com.produck.domain.SplitBookDetailAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.SplitBookDetail;
import com.produck.domain.enumeration.JoinerRole;
import com.produck.domain.enumeration.TransactionType;
import com.produck.repository.SplitBookDetailRepository;
import com.produck.service.dto.SplitBookDetailDTO;
import com.produck.service.mapper.SplitBookDetailMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SplitBookDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SplitBookDetailResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.INCOME;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.EXPENSE;

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final JoinerRole DEFAULT_JOINER_ROLE = JoinerRole.JOINER;
    private static final JoinerRole UPDATED_JOINER_ROLE = JoinerRole.PAYER;

    private static final String ENTITY_API_URL = "/api/split-book-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SplitBookDetailRepository splitBookDetailRepository;

    @Autowired
    private SplitBookDetailMapper splitBookDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSplitBookDetailMockMvc;

    private SplitBookDetail splitBookDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBookDetail createEntity(EntityManager em) {
        SplitBookDetail splitBookDetail = new SplitBookDetail()
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .personName(DEFAULT_PERSON_NAME)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .groupId(DEFAULT_GROUP_ID)
            .joinerRole(DEFAULT_JOINER_ROLE);
        return splitBookDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplitBookDetail createUpdatedEntity(EntityManager em) {
        SplitBookDetail splitBookDetail = new SplitBookDetail()
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .personName(UPDATED_PERSON_NAME)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .groupId(UPDATED_GROUP_ID)
            .joinerRole(UPDATED_JOINER_ROLE);
        return splitBookDetail;
    }

    @BeforeEach
    public void initTest() {
        splitBookDetail = createEntity(em);
    }

    @Test
    @Transactional
    void createSplitBookDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);
        var returnedSplitBookDetailDTO = om.readValue(
            restSplitBookDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SplitBookDetailDTO.class
        );

        // Validate the SplitBookDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSplitBookDetail = splitBookDetailMapper.toEntity(returnedSplitBookDetailDTO);
        assertSplitBookDetailUpdatableFieldsEquals(returnedSplitBookDetail, getPersistedSplitBookDetail(returnedSplitBookDetail));
    }

    @Test
    @Transactional
    void createSplitBookDetailWithExistingId() throws Exception {
        // Create the SplitBookDetail with an existing ID
        splitBookDetail.setId(1L);
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSplitBookDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSplitBookDetails() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        // Get all the splitBookDetailList
        restSplitBookDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(splitBookDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID)))
            .andExpect(jsonPath("$.[*].joinerRole").value(hasItem(DEFAULT_JOINER_ROLE.toString())));
    }

    @Test
    @Transactional
    void getSplitBookDetail() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        // Get the splitBookDetail
        restSplitBookDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, splitBookDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(splitBookDetail.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.personName").value(DEFAULT_PERSON_NAME))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID))
            .andExpect(jsonPath("$.joinerRole").value(DEFAULT_JOINER_ROLE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSplitBookDetail() throws Exception {
        // Get the splitBookDetail
        restSplitBookDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSplitBookDetail() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookDetail
        SplitBookDetail updatedSplitBookDetail = splitBookDetailRepository.findById(splitBookDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSplitBookDetail are not directly saved in db
        em.detach(updatedSplitBookDetail);
        updatedSplitBookDetail
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .personName(UPDATED_PERSON_NAME)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .groupId(UPDATED_GROUP_ID)
            .joinerRole(UPDATED_JOINER_ROLE);
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(updatedSplitBookDetail);

        restSplitBookDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSplitBookDetailToMatchAllProperties(updatedSplitBookDetail);
    }

    @Test
    @Transactional
    void putNonExistingSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splitBookDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splitBookDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splitBookDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSplitBookDetailWithPatch() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookDetail using partial update
        SplitBookDetail partialUpdatedSplitBookDetail = new SplitBookDetail();
        partialUpdatedSplitBookDetail.setId(splitBookDetail.getId());

        partialUpdatedSplitBookDetail.transactionDate(UPDATED_TRANSACTION_DATE).joinerRole(UPDATED_JOINER_ROLE);

        restSplitBookDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBookDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBookDetail))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSplitBookDetail, splitBookDetail),
            getPersistedSplitBookDetail(splitBookDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateSplitBookDetailWithPatch() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splitBookDetail using partial update
        SplitBookDetail partialUpdatedSplitBookDetail = new SplitBookDetail();
        partialUpdatedSplitBookDetail.setId(splitBookDetail.getId());

        partialUpdatedSplitBookDetail
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .personName(UPDATED_PERSON_NAME)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .groupId(UPDATED_GROUP_ID)
            .joinerRole(UPDATED_JOINER_ROLE);

        restSplitBookDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplitBookDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplitBookDetail))
            )
            .andExpect(status().isOk());

        // Validate the SplitBookDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplitBookDetailUpdatableFieldsEquals(
            partialUpdatedSplitBookDetail,
            getPersistedSplitBookDetail(partialUpdatedSplitBookDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, splitBookDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splitBookDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSplitBookDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splitBookDetail.setId(longCount.incrementAndGet());

        // Create the SplitBookDetail
        SplitBookDetailDTO splitBookDetailDTO = splitBookDetailMapper.toDto(splitBookDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplitBookDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(splitBookDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplitBookDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSplitBookDetail() throws Exception {
        // Initialize the database
        splitBookDetailRepository.saveAndFlush(splitBookDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the splitBookDetail
        restSplitBookDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, splitBookDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return splitBookDetailRepository.count();
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

    protected SplitBookDetail getPersistedSplitBookDetail(SplitBookDetail splitBookDetail) {
        return splitBookDetailRepository.findById(splitBookDetail.getId()).orElseThrow();
    }

    protected void assertPersistedSplitBookDetailToMatchAllProperties(SplitBookDetail expectedSplitBookDetail) {
        assertSplitBookDetailAllPropertiesEquals(expectedSplitBookDetail, getPersistedSplitBookDetail(expectedSplitBookDetail));
    }

    protected void assertPersistedSplitBookDetailToMatchUpdatableProperties(SplitBookDetail expectedSplitBookDetail) {
        assertSplitBookDetailAllUpdatablePropertiesEquals(expectedSplitBookDetail, getPersistedSplitBookDetail(expectedSplitBookDetail));
    }
}
