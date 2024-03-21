package com.produck.service.impl;

import com.produck.domain.Objective;
import com.produck.repository.ObjectiveRepository;
import com.produck.service.ObjectiveService;
import com.produck.service.dto.ObjectiveDTO;
import com.produck.service.mapper.ObjectiveMapper;
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
 * Service Implementation for managing {@link com.produck.domain.Objective}.
 */
@Service
@Transactional
public class ObjectiveServiceImpl implements ObjectiveService {

    private final Logger log = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    private final ObjectiveRepository objectiveRepository;

    private final ObjectiveMapper objectiveMapper;

    public ObjectiveServiceImpl(ObjectiveRepository objectiveRepository, ObjectiveMapper objectiveMapper) {
        this.objectiveRepository = objectiveRepository;
        this.objectiveMapper = objectiveMapper;
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

    /**
     *  Get all the objectives where Transaction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ObjectiveDTO> findAllWhereTransactionIsNull() {
        log.debug("Request to get all objectives where Transaction is null");
        return StreamSupport.stream(objectiveRepository.findAll().spliterator(), false)
            .filter(objective -> objective.getTransaction() == null)
            .map(objectiveMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
}
