package com.produck.service.impl;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentMethod;
import com.produck.domain.enumeration.PaymentCategoryCode;
import com.produck.repository.PaymentCategoryRepository;
import com.produck.repository.PaymentMethodRepository;
import com.produck.repository.TransactionRepository;
import com.produck.service.PaymentMethodService;
import com.produck.service.TransactionService;
import com.produck.service.dto.PaymentMethodDTO;
import com.produck.service.mapper.PaymentMethodMapper;
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
 * Service Implementation for managing {@link com.produck.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    private final TransactionRepository transactionRepository;

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final TransactionService transactionService;

    public PaymentMethodServiceImpl(
        PaymentMethodRepository paymentMethodRepository,
        PaymentMethodMapper paymentMethodMapper,
        TransactionService transactionService,
        TransactionRepository transactionRepository,
        PaymentCategoryRepository paymentCategoryRepository
    ) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.paymentCategoryRepository = paymentCategoryRepository;
    }

    @Override
    public PaymentMethodDTO save(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        Ledger ledger = paymentMethod.getLedger();
        transactionService.createInitTransaction(
            ledger,
            paymentMethod,
            paymentCategoryRepository.findByLedgerAndCode(ledger, PaymentCategoryCode.INIT.name()).get()
        );
        return paymentMethodMapper.toDto(paymentMethod);
    }

    @Override
    public PaymentMethodDTO saveByLedger(Ledger ledger, PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod.setIsHidden(false);
        paymentMethod.setLedger(ledger);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        transactionService.createInitTransaction(
            ledger,
            paymentMethod,
            paymentCategoryRepository.findByLedgerAndCode(ledger, PaymentCategoryCode.INIT.name()).get()
        );
        return paymentMethodMapper.toDto(paymentMethod);
    }

    @Override
    public PaymentMethodDTO update(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to update PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.toDto(paymentMethod);
    }

    @Override
    public Optional<PaymentMethodDTO> partialUpdate(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to partially update PaymentMethod : {}", paymentMethodDTO);

        return paymentMethodRepository
            .findById(paymentMethodDTO.getId())
            .map(existingPaymentMethod -> {
                paymentMethodMapper.partialUpdate(existingPaymentMethod, paymentMethodDTO);

                return existingPaymentMethod;
            })
            .map(paymentMethodRepository::save)
            .map(paymentMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payment Methods");
        return paymentMethodRepository.findAll(pageable).map(paymentMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMethodDTO> findOne(Long id) {
        log.debug("Request to get Payment Method : {}", id);
        return paymentMethodRepository.findById(id).map(paymentMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment Method : {}", id);
        paymentMethodRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> findAllByLedger(Ledger ledger, Pageable pageable) {
        log.debug("Request to get all Payment Methods By Ledger");
        return paymentMethodRepository.findAllByLedgerAndIsHiddenIsFalse(ledger, pageable).map(paymentMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findAllByLedger(Ledger ledger) {
        log.debug("Request to get all Payment Methods By Ledger");
        return paymentMethodRepository
            .findAllByLedgerAndIsHiddenIsFalse(ledger)
            .stream()
            .map(paymentMethodMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMethodDTO> findOneByLedger(Ledger ledger, Long id) {
        log.debug("Request to get Payment Method By Ledger: {}", id);
        return paymentMethodRepository.findOneByLedgerAndIdAndIsHiddenIsFalse(ledger, id).map(paymentMethodMapper::toDto);
    }

    @Override
    public void deleteByLedger(Ledger ledger, Long id) {
        log.debug("Request to delete Payment Method By Ledger: {} and Id: {}", ledger, id);
        if (transactionRepository.existsByLedgerAndPaymentMethodId(ledger, id)) {
            paymentMethodRepository.hiddenByLedgerAndId(ledger, id);
        } else {
            paymentMethodRepository.deleteByLedgerAndId(ledger, id);
        }
    }

    @Override
    public List<PaymentMethodDTO> findAllExistingByLedger(Ledger ledger) {
        log.debug("Request to get all existing Payment Methods By Ledger");
        return paymentMethodRepository
            .findAllExistingByLedger(ledger)
            .stream()
            .map(paymentMethodMapper::toDto)
            .collect(Collectors.toList());
    }
}
