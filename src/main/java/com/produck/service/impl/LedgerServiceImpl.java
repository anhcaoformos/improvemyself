package com.produck.service.impl;

import com.produck.config.Constants;
import com.produck.domain.Ledger;
import com.produck.domain.PaymentCategory;
import com.produck.domain.PaymentMethod;
import com.produck.domain.User;
import com.produck.domain.enumeration.PaymentCategoryCode;
import com.produck.domain.enumeration.PaymentMethodType;
import com.produck.repository.LedgerRepository;
import com.produck.service.LedgerService;
import com.produck.service.TransactionService;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.mapper.LedgerMapper;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * Service Implementation for managing {@link com.produck.domain.Ledger}.
 */
@Service
@Transactional
public class LedgerServiceImpl implements LedgerService {

    private final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);

    private final LedgerRepository ledgerRepository;

    private final LedgerMapper ledgerMapper;

    private final MessageSource messageSource;

    private final TransactionService transactionService;

    public LedgerServiceImpl(
        LedgerRepository ledgerRepository,
        LedgerMapper ledgerMapper,
        TransactionService transactionService,
        MessageSource messageSource
    ) {
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
        this.transactionService = transactionService;
        this.messageSource = messageSource;
    }

    @Override
    public LedgerDTO save(LedgerDTO ledgerDTO) {
        log.debug("Request to save Ledger : {}", ledgerDTO);
        Ledger ledger = ledgerMapper.toEntity(ledgerDTO);
        ledger = ledgerRepository.save(ledger);
        return ledgerMapper.toDto(ledger);
    }

    @Override
    public LedgerDTO save(User user, LedgerDTO ledgerDTO) {
        log.debug("Request to save Ledger by user : {}", ledgerDTO);
        return ledgerMapper.toDto(createLedgerData(user, ledgerDTO.getName()));
    }

    @Override
    public LedgerDTO update(LedgerDTO ledgerDTO) {
        log.debug("Request to update Ledger : {}", ledgerDTO);
        Ledger ledger = ledgerMapper.toEntity(ledgerDTO);
        ledger = ledgerRepository.save(ledger);
        return ledgerMapper.toDto(ledger);
    }

    @Override
    public Optional<LedgerDTO> partialUpdate(LedgerDTO ledgerDTO) {
        log.debug("Request to partially update Ledger : {}", ledgerDTO);

        return ledgerRepository
            .findById(ledgerDTO.getId())
            .map(existingLedger -> {
                ledgerMapper.partialUpdate(existingLedger, ledgerDTO);

                return existingLedger;
            })
            .map(ledgerRepository::save)
            .map(ledgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ledgers");
        return ledgerRepository.findAll(pageable).map(ledgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerDTO> findOne(Long id) {
        log.debug("Request to get Ledger : {}", id);
        return ledgerRepository.findById(id).map(ledgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerDTO> findDefaultByUser(User user) {
        log.debug("Request to get Default Ledger by User : {}", user.getId());
        return ledgerRepository.findOneByUserAndIsDefaultIsTrue(user).map(ledgerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ledger : {}", id);
        ledgerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerDTO> findAllByUser(User user, Pageable pageable) {
        log.debug("Request to get all Ledgers By User");
        return ledgerRepository.findAllByUser(user, pageable).map(ledgerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerDTO> findOneByUser(User user, Long id) {
        log.debug("Request to get Ledger By User: {}", id);
        return ledgerRepository.findOneByUserAndId(user, id).map(ledgerMapper::toDto);
    }

    @Override
    public void deleteByUser(User user, Long id) {
        log.debug("Request to delete Ledger By User: {}", id);
        transactionService.deleteAllByLedgerId(id);

        ledgerRepository.deleteByUserAndId(user, id);
    }

    @Override
    public void setDefault(User user, Long id) {
        log.debug("Request to set default Ledger : {}", id);
        if (!ledgerRepository.existsByUserAndIdAndIsDefaultIsTrue(user, id)) {
            ledgerRepository.resetDefaultByUserAndId(user);
            ledgerRepository.setDefaultByUserAndId(user, id);
        }
    }

    @Override
    public Ledger createLedgerData(User user, String ledgerName) {
        if (Objects.nonNull(user) && Objects.nonNull(user.getId())) {
            ledgerRepository.resetDefaultByUserAndId(user);
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCode(PaymentMethodType.CASH.name());
        paymentMethod.setName(
            messageSource.getMessage("payment.method.type." + PaymentMethodType.CASH.name(), null, Locale.forLanguageTag(user.getLangKey()))
        );
        paymentMethod.setType(PaymentMethodType.CASH);
        paymentMethod.setIsHidden(Boolean.FALSE);

        Ledger ledger = new Ledger();
        if (ObjectUtils.isEmpty(ledgerName)) {
            ledger.setName(Constants.DEFAULT);
        } else {
            ledger.setName(ledgerName);
        }
        ledger.setIsDefault(Boolean.TRUE);
        ledger.addPaymentMethod(paymentMethod);
        ledger.setUser(user);
        PaymentCategory initPaymentCategory = null;
        for (PaymentCategoryCode paymentCategoryCode : PaymentCategoryCode.values()) {
            PaymentCategory paymentCategory = new PaymentCategory();
            paymentCategory.setCode(paymentCategoryCode.name());
            paymentCategory.setName(
                messageSource.getMessage(
                    "payment.category.type." + paymentCategoryCode.name(),
                    null,
                    Locale.forLanguageTag(ledger.getUser().getLangKey())
                )
            );
            paymentCategory.setIsHidden(false);
            paymentCategory.setIsDefault(true);
            paymentCategory.setLedger(ledger);
            if (PaymentCategoryCode.INIT.equals(paymentCategoryCode)) {
                paymentCategory.setIsHidden(false);
                initPaymentCategory = paymentCategory;
            }
            ledger.addPaymentCategory(paymentCategory);
        }

        transactionService.createInitTransaction(ledger, paymentMethod, initPaymentCategory);
        return ledgerRepository.save(ledger);
    }

    @Override
    public List<LedgerDTO> findAllByUser(User user) {
        return ledgerRepository.findAllByUser(user).stream().map(ledgerMapper::toDto).collect(Collectors.toList());
    }
}
