package com.produck.web.rest;

import static com.produck.domain.PaymentCategoryAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.PaymentCategory;
import com.produck.repository.PaymentCategoryRepository;
import com.produck.service.dto.PaymentCategoryDTO;
import com.produck.service.mapper.PaymentCategoryMapper;
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
 * Integration tests for the {@link PaymentCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    private static final String ENTITY_API_URL = "/api/payment-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentCategoryRepository paymentCategoryRepository;

    @Autowired
    private PaymentCategoryMapper paymentCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentCategoryMockMvc;

    private PaymentCategory paymentCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCategory createEntity(EntityManager em) {
        PaymentCategory paymentCategory = new PaymentCategory()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .isDefault(DEFAULT_IS_DEFAULT)
            .isHidden(DEFAULT_IS_HIDDEN);
        return paymentCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCategory createUpdatedEntity(EntityManager em) {
        PaymentCategory paymentCategory = new PaymentCategory()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .isHidden(UPDATED_IS_HIDDEN);
        return paymentCategory;
    }

    @BeforeEach
    public void initTest() {
        paymentCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);
        var returnedPaymentCategoryDTO = om.readValue(
            restPaymentCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentCategoryDTO.class
        );

        // Validate the PaymentCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentCategory = paymentCategoryMapper.toEntity(returnedPaymentCategoryDTO);
        assertPaymentCategoryUpdatableFieldsEquals(returnedPaymentCategory, getPersistedPaymentCategory(returnedPaymentCategory));
    }

    @Test
    @Transactional
    void createPaymentCategoryWithExistingId() throws Exception {
        // Create the PaymentCategory with an existing ID
        paymentCategory.setId(1L);
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentCategories() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        // Get all the paymentCategoryList
        restPaymentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    void getPaymentCategory() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        // Get the paymentCategory
        restPaymentCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentCategory() throws Exception {
        // Get the paymentCategory
        restPaymentCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentCategory() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentCategory
        PaymentCategory updatedPaymentCategory = paymentCategoryRepository.findById(paymentCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentCategory are not directly saved in db
        em.detach(updatedPaymentCategory);
        updatedPaymentCategory.code(UPDATED_CODE).name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT).isHidden(UPDATED_IS_HIDDEN);
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(updatedPaymentCategory);

        restPaymentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentCategoryToMatchAllProperties(updatedPaymentCategory);
    }

    @Test
    @Transactional
    void putNonExistingPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentCategoryWithPatch() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentCategory using partial update
        PaymentCategory partialUpdatedPaymentCategory = new PaymentCategory();
        partialUpdatedPaymentCategory.setId(paymentCategory.getId());

        partialUpdatedPaymentCategory.code(UPDATED_CODE).name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT).isHidden(UPDATED_IS_HIDDEN);

        restPaymentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentCategory))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentCategory, paymentCategory),
            getPersistedPaymentCategory(paymentCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentCategoryWithPatch() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentCategory using partial update
        PaymentCategory partialUpdatedPaymentCategory = new PaymentCategory();
        partialUpdatedPaymentCategory.setId(paymentCategory.getId());

        partialUpdatedPaymentCategory.code(UPDATED_CODE).name(UPDATED_NAME).isDefault(UPDATED_IS_DEFAULT).isHidden(UPDATED_IS_HIDDEN);

        restPaymentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentCategory))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentCategoryUpdatableFieldsEquals(
            partialUpdatedPaymentCategory,
            getPersistedPaymentCategory(partialUpdatedPaymentCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentCategory.setId(longCount.incrementAndGet());

        // Create the PaymentCategory
        PaymentCategoryDTO paymentCategoryDTO = paymentCategoryMapper.toDto(paymentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentCategory() throws Exception {
        // Initialize the database
        paymentCategoryRepository.saveAndFlush(paymentCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentCategory
        restPaymentCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentCategoryRepository.count();
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

    protected PaymentCategory getPersistedPaymentCategory(PaymentCategory paymentCategory) {
        return paymentCategoryRepository.findById(paymentCategory.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentCategoryToMatchAllProperties(PaymentCategory expectedPaymentCategory) {
        assertPaymentCategoryAllPropertiesEquals(expectedPaymentCategory, getPersistedPaymentCategory(expectedPaymentCategory));
    }

    protected void assertPersistedPaymentCategoryToMatchUpdatableProperties(PaymentCategory expectedPaymentCategory) {
        assertPaymentCategoryAllUpdatablePropertiesEquals(expectedPaymentCategory, getPersistedPaymentCategory(expectedPaymentCategory));
    }
}
