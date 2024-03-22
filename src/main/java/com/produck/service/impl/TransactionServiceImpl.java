package com.produck.service.impl;

import com.produck.config.Constants;
import com.produck.domain.*;
import com.produck.domain.enumeration.ObjectiveType;
import com.produck.domain.enumeration.TransactionType;
import com.produck.repository.ObjectiveRepository;
import com.produck.repository.TransactionRepository;
import com.produck.service.ObjectiveService;
import com.produck.service.TransactionService;
import com.produck.service.dto.AssetBalanceDTO;
import com.produck.service.dto.ObjectivePayBookDTO;
import com.produck.service.dto.SummaryPaymentCategoryDTO;
import com.produck.service.dto.TransactionDTO;
import com.produck.service.mapper.TransactionMapper;
import com.produck.service.utils.DateTimeUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.produck.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    private final MessageSource messageSource;

    private final ObjectiveService objectiveService;

    private final ObjectiveRepository objectiveRepository;

    public TransactionServiceImpl(
        TransactionRepository transactionRepository,
        TransactionMapper transactionMapper,
        ObjectiveService objectiveService,
        ObjectiveRepository objectiveRepository,
        MessageSource messageSource
    ) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
        this.messageSource = messageSource;
    }

    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {
        log.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        if (!TransactionType.INTERNAL.equals(transaction.getTransactionType())) {
            if (Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transaction.getPaymentCategory().getCode())) {
                transaction.getObjective().setType(ObjectiveType.PAY_BOOK);
            }
            transaction.setObjective(objectiveService.createIfNew(transaction, transaction.getObjective()));
        }
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDTO update(TransactionDTO transactionDTO) {
        log.debug("Request to update Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        if (!TransactionType.INTERNAL.equals(transaction.getTransactionType())) {
            if (Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(transaction.getPaymentCategory().getCode())) {
                transaction.getObjective().setType(ObjectiveType.PAY_BOOK);
            }
            transaction.setObjective(objectiveService.createIfNew(transaction, transaction.getObjective()));
        }

        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public Optional<TransactionDTO> partialUpdate(TransactionDTO transactionDTO) {
        log.debug("Request to partially update Transaction : {}", transactionDTO);

        return transactionRepository
            .findById(transactionDTO.getId())
            .map(existingTransaction -> {
                transactionMapper.partialUpdate(existingTransaction, transactionDTO);

                return existingTransaction;
            })
            .map(transactionRepository::save)
            .map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable).map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id).map(transactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDTO> findAllByLedger(Ledger ledger, Pageable pageable) {
        log.debug("Request to get all Transactions By Ledger");
        return transactionRepository.findAllByLedger(ledger, pageable).map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionDTO> findAllBy(
        Ledger ledger,
        LocalDate transactionDate,
        TransactionType type,
        Long paymentCategoryId,
        Long paymentMethodId,
        Pageable pageable
    ) {
        log.debug(
            "Request to get all Transactions By Ledger {}, Date: {}, Type: {}, Payment Category Id: {}, Payment Method Id: {}",
            ledger,
            transactionDate,
            type,
            paymentCategoryId,
            paymentMethodId
        );

        return transactionRepository
            .findAllBy(ledger, transactionDate, type, paymentCategoryId, paymentMethodId, pageable)
            .map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findOneByLedger(Ledger ledger, Long id) {
        log.debug("Request to get Transactions By Ledger: {}", id);
        return transactionRepository.findOneByLedgerAndId(ledger, id).map(transactionMapper::toDto);
    }

    @Override
    public void deleteByLedger(Ledger ledger, Long id) {
        log.debug("Request to delete Transactions By Ledger: {}", id);
        transactionRepository.deleteByLedgerAndId(ledger, id);
    }

    @Override
    public void deleteAllByLedgerId(Long ledgerId) {
        log.debug("Request to delete all Transactions By Ledger Id: {}", ledgerId);
        transactionRepository.deleteAllByLedgerId(ledgerId);
    }

    @Override
    public Page<AssetBalanceDTO> findAllAssetBalanceByLedger(Ledger ledger, Pageable pageable) {
        return transactionRepository.findCurrentAssetBalanceByLedger(ledger.getId(), pageable);
    }

    @Override
    public Page<ObjectivePayBookDTO> findAllObjectivePayBookByLedger(Ledger ledger, Pageable pageable) {
        return transactionRepository.findObjectivePayBookByLedger(ledger.getId(), pageable);
    }

    @Override
    public List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Ledger ledger) {
        return transactionRepository.findSummaryPaymentCategories(ledger.getId());
    }

    @Override
    public List<SummaryPaymentCategoryDTO> findSummaryPaymentCategories(Ledger ledger, LocalDate month) {
        return transactionRepository.findSummaryPaymentCategories(ledger.getId(), month);
    }

    @Override
    public List<LocalDate> getFilterTransactionDates(Ledger ledger) {
        LocalDate oldestTransactionDate = transactionRepository
            .getFirstByLedgerOrderByTransactionDate(ledger)
            .map(Transaction::getTransactionDate)
            .orElse(LocalDate.now());
        return DateTimeUtils.generateLocalDateMonthsList(oldestTransactionDate);
    }

    @Override
    public void createInitTransaction(Ledger ledger, PaymentMethod paymentMethod, PaymentCategory paymentCategory) {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.ZERO);
        String langKey = ledger.getUser().getLangKey();
        transaction.setTransactionDate(LocalDate.now());
        Optional<Objective> defaultObjective = Optional.empty();
        if (Objects.nonNull(ledger.getId())) {
            defaultObjective = objectiveRepository.findDefaultObjective(ledger);
        }
        Objective objective = defaultObjective.orElseGet(
            () -> objectiveService.createBy(ledger, paymentCategory, getMessageByKey("ledger.owner", langKey), ObjectiveType.DEFAULT)
        );
        transaction.setObjective(objective);
        transaction.setDescription(getMessageByKey("payment.category.type.INIT", langKey));
        transaction.setLedger(ledger);
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setPaymentCategory(paymentCategory);
        this.transactionRepository.save(transaction);
    }

    private String getMessageByKey(String key, String langKey) {
        return messageSource.getMessage("ledger.owner", null, Locale.forLanguageTag(langKey));
    }
}
