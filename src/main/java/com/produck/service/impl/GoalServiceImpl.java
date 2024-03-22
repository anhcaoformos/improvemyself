package com.produck.service.impl;

import com.produck.domain.Goal;
import com.produck.domain.Ledger;
import com.produck.repository.GoalRepository;
import com.produck.service.GoalService;
import com.produck.service.dto.GoalDTO;
import com.produck.service.mapper.GoalMapper;
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
 * Service Implementation for managing {@link com.produck.domain.Goal}.
 */
@Service
@Transactional
public class GoalServiceImpl implements GoalService {

    private final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);

    private final GoalRepository goalRepository;

    private final GoalMapper goalMapper;

    public GoalServiceImpl(GoalRepository goalRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
    }

    @Override
    public GoalDTO save(GoalDTO goalDTO) {
        log.debug("Request to save Goal : {}", goalDTO);
        Goal goal = goalMapper.toEntity(goalDTO);
        goal = goalRepository.save(goal);
        return goalMapper.toDto(goal);
    }

    @Override
    public GoalDTO update(GoalDTO goalDTO) {
        log.debug("Request to update Goal : {}", goalDTO);
        Goal goal = goalMapper.toEntity(goalDTO);
        goal = goalRepository.save(goal);
        return goalMapper.toDto(goal);
    }

    @Override
    public Optional<GoalDTO> partialUpdate(GoalDTO goalDTO) {
        log.debug("Request to partially update Goal : {}", goalDTO);

        return goalRepository
            .findById(goalDTO.getId())
            .map(existingGoal -> {
                goalMapper.partialUpdate(existingGoal, goalDTO);

                return existingGoal;
            })
            .map(goalRepository::save)
            .map(goalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GoalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Goals");
        return goalRepository.findAll(pageable).map(goalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalDTO> findOne(Long id) {
        log.debug("Request to get Goal : {}", id);
        return goalRepository.findById(id).map(goalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Goal : {}", id);
        goalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GoalDTO> findAllByLedger(Ledger ledger, Pageable pageable) {
        log.debug("Request to get all Goals By Ledger");
        return goalRepository.findAllByLedger(ledger, pageable).map(goalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalDTO> findAllByLedger(Ledger ledger) {
        log.debug("Request to get all Goals By Ledger");
        return goalRepository.findAllByLedger(ledger).stream().map(goalMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GoalDTO> findOneByLedger(Ledger ledger, Long id) {
        log.debug("Request to get Goal By Ledger: {}", id);
        return goalRepository.findOneByLedgerAndId(ledger, id).map(goalMapper::toDto);
    }

    @Override
    public void deleteByLedger(Ledger ledger, Long id) {
        log.debug("Request to delete Goal By Ledger: {}", id);
        goalRepository.deleteByLedgerAndId(ledger, id);
    }
}
