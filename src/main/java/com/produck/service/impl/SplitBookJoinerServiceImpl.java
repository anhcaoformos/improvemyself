package com.produck.service.impl;

import com.produck.domain.SplitBookJoiner;
import com.produck.repository.SplitBookJoinerRepository;
import com.produck.service.SplitBookJoinerService;
import com.produck.service.dto.SplitBookJoinerDTO;
import com.produck.service.mapper.SplitBookJoinerMapper;
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
 * Service Implementation for managing {@link com.produck.domain.SplitBookJoiner}.
 */
@Service
@Transactional
public class SplitBookJoinerServiceImpl implements SplitBookJoinerService {

    private final Logger log = LoggerFactory.getLogger(SplitBookJoinerServiceImpl.class);

    private final SplitBookJoinerRepository splitBookJoinerRepository;

    private final SplitBookJoinerMapper splitBookJoinerMapper;

    public SplitBookJoinerServiceImpl(SplitBookJoinerRepository splitBookJoinerRepository, SplitBookJoinerMapper splitBookJoinerMapper) {
        this.splitBookJoinerRepository = splitBookJoinerRepository;
        this.splitBookJoinerMapper = splitBookJoinerMapper;
    }

    @Override
    public SplitBookJoinerDTO save(SplitBookJoinerDTO splitBookJoinerDTO) {
        log.debug("Request to save SplitBookJoiner : {}", splitBookJoinerDTO);
        SplitBookJoiner splitBookJoiner = splitBookJoinerMapper.toEntity(splitBookJoinerDTO);
        splitBookJoiner = splitBookJoinerRepository.save(splitBookJoiner);
        return splitBookJoinerMapper.toDto(splitBookJoiner);
    }

    @Override
    public SplitBookJoinerDTO update(SplitBookJoinerDTO splitBookJoinerDTO) {
        log.debug("Request to update SplitBookJoiner : {}", splitBookJoinerDTO);
        SplitBookJoiner splitBookJoiner = splitBookJoinerMapper.toEntity(splitBookJoinerDTO);
        splitBookJoiner = splitBookJoinerRepository.save(splitBookJoiner);
        return splitBookJoinerMapper.toDto(splitBookJoiner);
    }

    @Override
    public Optional<SplitBookJoinerDTO> partialUpdate(SplitBookJoinerDTO splitBookJoinerDTO) {
        log.debug("Request to partially update SplitBookJoiner : {}", splitBookJoinerDTO);

        return splitBookJoinerRepository
            .findById(splitBookJoinerDTO.getId())
            .map(existingSplitBookJoiner -> {
                splitBookJoinerMapper.partialUpdate(existingSplitBookJoiner, splitBookJoinerDTO);

                return existingSplitBookJoiner;
            })
            .map(splitBookJoinerRepository::save)
            .map(splitBookJoinerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SplitBookJoinerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SplitBookJoiners");
        return splitBookJoinerRepository.findAll(pageable).map(splitBookJoinerMapper::toDto);
    }

    /**
     *  Get all the splitBookJoiners where SplitBookDetail is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SplitBookJoinerDTO> findAllWhereSplitBookDetailIsNull() {
        log.debug("Request to get all splitBookJoiners where SplitBookDetail is null");
        return StreamSupport.stream(splitBookJoinerRepository.findAll().spliterator(), false)
            .filter(splitBookJoiner -> splitBookJoiner.getSplitBookDetail() == null)
            .map(splitBookJoinerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SplitBookJoinerDTO> findOne(Long id) {
        log.debug("Request to get SplitBookJoiner : {}", id);
        return splitBookJoinerRepository.findById(id).map(splitBookJoinerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SplitBookJoiner : {}", id);
        splitBookJoinerRepository.deleteById(id);
    }
}
