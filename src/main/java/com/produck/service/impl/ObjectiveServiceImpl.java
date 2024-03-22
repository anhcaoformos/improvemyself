package com.produck.service.impl;

import com.produck.config.Constants;
import com.produck.domain.Ledger;
import com.produck.domain.Objective;
import com.produck.domain.PaymentCategory;
import com.produck.domain.Transaction;
import com.produck.domain.enumeration.ObjectiveType;
import com.produck.repository.ObjectiveRepository;
import com.produck.repository.TransactionRepository;
import com.produck.service.ObjectiveService;
import com.produck.service.dto.ObjectiveDTO;
import com.produck.service.mapper.ObjectiveMapper;
import com.produck.web.rest.errors.BadRequestAlertException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
 * Service Implementation for managing {@link com.produck.domain.Objective}.
 */
@Service
@Transactional
public class ObjectiveServiceImpl implements ObjectiveService {

    private final Logger log = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    private final ObjectiveRepository objectiveRepository;

    private final ObjectiveMapper objectiveMapper;

    private final TransactionRepository transactionRepository;

    public ObjectiveServiceImpl(
        ObjectiveRepository objectiveRepository,
        ObjectiveMapper objectiveMapper,
        TransactionRepository transactionRepository
    ) {
        this.objectiveRepository = objectiveRepository;
        this.objectiveMapper = objectiveMapper;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ObjectiveDTO save(ObjectiveDTO objectiveDTO) {
        log.debug("Request to save Objective : {}", objectiveDTO);
        Objective objective = objectiveMapper.toEntity(objectiveDTO);
        objective = objectiveRepository.save(objective);
        return objectiveMapper.toDto(objective);
    }

    @Override
    public ObjectiveDTO update(ObjectiveDTO objectiveDTO) {
        log.debug("Request to update Objective : {}", objectiveDTO);
        Objective objective = objectiveMapper.toEntity(objectiveDTO);
        objective = objectiveRepository.save(objective);
        return objectiveMapper.toDto(objective);
    }

    @Override
    public Optional<ObjectiveDTO> partialUpdate(ObjectiveDTO objectiveDTO) {
        log.debug("Request to partially update Objective : {}", objectiveDTO);

        return objectiveRepository
            .findById(objectiveDTO.getId())
            .map(existingObjective -> {
                objectiveMapper.partialUpdate(existingObjective, objectiveDTO);

                return existingObjective;
            })
            .map(objectiveRepository::save)
            .map(objectiveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ObjectiveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Objectives");
        return objectiveRepository.findAll(pageable).map(objectiveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ObjectiveDTO> findOne(Long id) {
        log.debug("Request to get Objective : {}", id);
        return objectiveRepository.findById(id).map(objectiveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Objective : {}", id);
        objectiveRepository.deleteById(id);
    }

    @Override
    public void deleteBy(Ledger ledger, Long id, ObjectiveType type) {
        log.debug("Request to delete Objective : {} by Ledger: {}", id, ledger);
        switch (type) {
            case TO_OBJECTIVE: {
                if (transactionRepository.existsByLedgerAndToObjectiveId(ledger, id)) {
                    objectiveRepository.hiddenByLedgerAndId(ledger, id);
                } else {
                    objectiveRepository.deleteByLedgerAndId(ledger, id);
                }
                break;
            }
            case FROM_OBJECTIVE: {
                if (transactionRepository.existsByLedgerAndFromObjectiveId(ledger, id)) {
                    objectiveRepository.hiddenByLedgerAndId(ledger, id);
                } else {
                    objectiveRepository.deleteByLedgerAndId(ledger, id);
                }
                break;
            }
        }
    }

    @Override
    public Objective createBy(Ledger ledger, PaymentCategory paymentCategory, String name, ObjectiveType type) {
        if (
            Objects.nonNull(ledger.getId()) &&
            objectiveRepository.findByLedgerAndPaymentCategoryAndNameAndType(ledger, paymentCategory, name, type).isPresent()
        ) {
            throw new BadRequestAlertException("A new objective cannot already have an ID", "objective", "idexists");
        }
        Objective objective = new Objective();
        objective.setName(name);
        objective.setType(type);
        objective.setIsHidden(Boolean.FALSE);
        objective.setPaymentCategory(paymentCategory);
        objective.setLedger(ledger);
        return objectiveRepository.save(objective);
    }

    @Override
    public Objective createIfNew(Transaction transaction, Objective objective) {
        if (Objects.nonNull(objective) && Objects.isNull(objective.getId())) {
            return createBy(transaction.getLedger(), transaction.getPaymentCategory(), objective.getName(), objective.getType());
        }
        return objective;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectiveDTO> findAllBy(Ledger ledger, PaymentCategory paymentCategory, ObjectiveType type) {
        log.debug("Request to get all Objectives by Ledger: {} and Payment Category: {}", ledger, paymentCategory);
        if (Objects.isNull(paymentCategory)) {
            return objectiveRepository
                .findAllByLedgerAndTypeAndIsHiddenIsFalse(ledger, type)
                .stream()
                .map(objectiveMapper::toDto)
                .collect(Collectors.toList());
        }
        if (Constants.PAY_BOOK_PAYMENT_CATEGORIES.contains(paymentCategory.getCode())) {
            return objectiveRepository
                .findAllByLedgerAndPaymentCategoryCodeInAndIsHiddenIsFalse(ledger, Constants.PAY_BOOK_PAYMENT_CATEGORIES)
                .stream()
                .map(objectiveMapper::toDto)
                .collect(Collectors.toList());
        }
        return objectiveRepository
            .findAllByLedgerAndPaymentCategoryAndTypeAndIsHiddenIsFalse(ledger, paymentCategory, type)
            .stream()
            .map(objectiveMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ObjectiveDTO> findDefaultObjective(Ledger ledger) {
        return objectiveRepository.findDefaultObjective(ledger).map(objectiveMapper::toDto);
    }
}
