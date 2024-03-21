package com.produck.service.impl;

import com.produck.domain.PaymentCategory;
import com.produck.repository.PaymentCategoryRepository;
import com.produck.service.PaymentCategoryService;
import com.produck.service.dto.PaymentCategoryDTO;
import com.produck.service.mapper.PaymentCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.produck.domain.PaymentCategory}.
 */
@Service
@Transactional
public class PaymentCategoryServiceImpl implements PaymentCategoryService {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryServiceImpl.class);

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final PaymentCategoryMapper paymentCategoryMapper;

    public PaymentCategoryServiceImpl(PaymentCategoryRepository paymentCategoryRepository, PaymentCategoryMapper paymentCategoryMapper) {
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryMapper = paymentCategoryMapper;
    }

    @Override
    public PaymentCategoryDTO save(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to save PaymentCategory : {}", paymentCategoryDTO);
        PaymentCategory paymentCategory = paymentCategoryMapper.toEntity(paymentCategoryDTO);
        paymentCategory = paymentCategoryRepository.save(paymentCategory);
        return paymentCategoryMapper.toDto(paymentCategory);
    }

    @Override
    public PaymentCategoryDTO update(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to update PaymentCategory : {}", paymentCategoryDTO);
        PaymentCategory paymentCategory = paymentCategoryMapper.toEntity(paymentCategoryDTO);
        paymentCategory = paymentCategoryRepository.save(paymentCategory);
        return paymentCategoryMapper.toDto(paymentCategory);
    }

    @Override
    public Optional<PaymentCategoryDTO> partialUpdate(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to partially update PaymentCategory : {}", paymentCategoryDTO);

        return paymentCategoryRepository
            .findById(paymentCategoryDTO.getId())
            .map(existingPaymentCategory -> {
                paymentCategoryMapper.partialUpdate(existingPaymentCategory, paymentCategoryDTO);

                return existingPaymentCategory;
            })
            .map(paymentCategoryRepository::save)
            .map(paymentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCategories");
        return paymentCategoryRepository.findAll(pageable).map(paymentCategoryMapper::toDto);
    }

    /**
     *  Get all the paymentCategories where Transaction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentCategoryDTO> findAllWhereTransactionIsNull() {
        log.debug("Request to get all paymentCategories where Transaction is null");
        return StreamSupport.stream(paymentCategoryRepository.findAll().spliterator(), false)
            .filter(paymentCategory -> paymentCategory.getTransaction() == null)
            .map(paymentCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the paymentCategories where Objective is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentCategoryDTO> findAllWhereObjectiveIsNull() {
        log.debug("Request to get all paymentCategories where Objective is null");
        return StreamSupport.stream(paymentCategoryRepository.findAll().spliterator(), false)
            .filter(paymentCategory -> paymentCategory.getObjective() == null)
            .map(paymentCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOne(Long id) {
        log.debug("Request to get PaymentCategory : {}", id);
        return paymentCategoryRepository.findById(id).map(paymentCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentCategory : {}", id);
        paymentCategoryRepository.deleteById(id);
    }
}
