package com.produck.service.impl;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentCategory;
import com.produck.repository.PaymentCategoryRepository;
import com.produck.repository.TransactionRepository;
import com.produck.service.PaymentCategoryService;
import com.produck.service.dto.PaymentCategoryDTO;
import com.produck.service.mapper.PaymentCategoryMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final TransactionRepository transactionRepository;

    public PaymentCategoryServiceImpl(
        PaymentCategoryRepository paymentCategoryRepository,
        PaymentCategoryMapper paymentCategoryMapper,
        TransactionRepository transactionRepository
    ) {
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryMapper = paymentCategoryMapper;
        this.transactionRepository = transactionRepository;
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
        log.debug("Request to get all Payment Categories");
        return paymentCategoryRepository.findAll(pageable).map(paymentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOne(Long id) {
        log.debug("Request to get Payment Category : {}", id);
        return paymentCategoryRepository.findById(id).map(paymentCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment Category : {}", id);
        paymentCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findAllByLedger(Ledger ledger, Pageable pageable) {
        log.debug("Request to get all Payment Categories By Ledger");
        return paymentCategoryRepository.findAllByLedger(ledger, pageable).map(paymentCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentCategoryDTO> findAllByLedger(Ledger ledger) {
        log.debug("Request to get all Payment Categories By Ledger");
        return paymentCategoryRepository.findAllByLedger(ledger).stream().map(paymentCategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentCategoryDTO> findAllShowByLedger(Ledger ledger) {
        log.debug("Request to get all show Payment Categories By Ledger");
        return paymentCategoryRepository
            .findAllByLedgerAndIsHiddenIsFalse(ledger)
            .stream()
            .map(paymentCategoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOneByLedger(Ledger ledger, Long id) {
        log.debug("Request to get Payment Category By Ledger: {}", id);
        return paymentCategoryRepository.findOneByLedgerAndId(ledger, id).map(paymentCategoryMapper::toDto);
    }

    @Override
    public void deleteByLedger(Ledger ledger, Long id) {
        log.debug("Request to delete Payment Category By Ledger: {} and Id: {}", ledger, id);
        if (transactionRepository.existsByLedgerAndPaymentCategoryId(ledger, id)) {
            paymentCategoryRepository.hiddenByLedgerAndId(ledger, id);
        } else {
            paymentCategoryRepository.deleteByLedgerAndId(ledger, id);
        }
    }

    @Override
    public void hiddenByLedger(Ledger ledger, Long id) {
        log.debug("Request to hidden Payment Category By Ledger: {}", id);
        paymentCategoryRepository.hiddenByLedgerAndId(ledger, id);
    }

    @Override
    public void unhiddenByLedger(Ledger ledger, Long id) {
        log.debug("Request to unhidden Payment Category By Ledger: {}", id);
        paymentCategoryRepository.unhiddenByLedgerAndId(ledger, id);
    }

    @Override
    public List<PaymentCategoryDTO> findAllExistingByLedger(Ledger ledger) {
        log.debug("Request to get all existing Payment Categories By Ledger");
        return paymentCategoryRepository
            .findAllExistingByLedger(ledger)
            .stream()
            .map(paymentCategoryMapper::toDto)
            .collect(Collectors.toList());
    }
}
