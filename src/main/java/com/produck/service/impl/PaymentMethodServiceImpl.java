package com.produck.service.impl;

import com.produck.domain.PaymentMethod;
import com.produck.repository.PaymentMethodRepository;
import com.produck.service.PaymentMethodService;
import com.produck.service.dto.PaymentMethodDTO;
import com.produck.service.mapper.PaymentMethodMapper;
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
 * Service Implementation for managing {@link com.produck.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Override
    public PaymentMethodDTO save(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
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
        log.debug("Request to get all PaymentMethods");
        return paymentMethodRepository.findAll(pageable).map(paymentMethodMapper::toDto);
    }

    /**
     *  Get all the paymentMethods where Transaction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findAllWhereTransactionIsNull() {
        log.debug("Request to get all paymentMethods where Transaction is null");
        return StreamSupport.stream(paymentMethodRepository.findAll().spliterator(), false)
            .filter(paymentMethod -> paymentMethod.getTransaction() == null)
            .map(paymentMethodMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMethodDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id).map(paymentMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
    }
}
