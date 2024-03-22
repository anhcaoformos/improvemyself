package com.produck.service.impl;

import com.produck.domain.SplitBook;
import com.produck.domain.User;
import com.produck.repository.SplitBookRepository;
import com.produck.service.SplitBookService;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.mapper.SplitBookMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.produck.domain.SplitBook}.
 */
@Service
@Transactional
public class SplitBookServiceImpl implements SplitBookService {

    private final Logger log = LoggerFactory.getLogger(SplitBookServiceImpl.class);

    private final SplitBookRepository splitBookRepository;

    private final SplitBookMapper splitBookMapper;

    public SplitBookServiceImpl(SplitBookRepository splitBookRepository, SplitBookMapper splitBookMapper) {
        this.splitBookRepository = splitBookRepository;
        this.splitBookMapper = splitBookMapper;
    }

    @Override
    public SplitBookDTO save(SplitBookDTO splitBookDTO) {
        log.debug("Request to save SplitBook : {}", splitBookDTO);
        SplitBook splitBook = splitBookMapper.toEntity(splitBookDTO);
        splitBook = splitBookRepository.save(splitBook);
        return splitBookMapper.toDto(splitBook);
    }

    @Override
    public SplitBookDTO update(SplitBookDTO splitBookDTO) {
        log.debug("Request to update SplitBook : {}", splitBookDTO);
        SplitBook splitBook = splitBookMapper.toEntity(splitBookDTO);
        splitBook = splitBookRepository.save(splitBook);
        return splitBookMapper.toDto(splitBook);
    }

    @Override
    public Optional<SplitBookDTO> partialUpdate(SplitBookDTO splitBookDTO) {
        log.debug("Request to partially update SplitBook : {}", splitBookDTO);

        return splitBookRepository
            .findById(splitBookDTO.getId())
            .map(existingSplitBook -> {
                splitBookMapper.partialUpdate(existingSplitBook, splitBookDTO);

                return existingSplitBook;
            })
            .map(splitBookRepository::save)
            .map(splitBookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SplitBookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SplitBooks");
        return splitBookRepository.findAll(pageable).map(splitBookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SplitBookDTO> findOne(Long id) {
        log.debug("Request to get SplitBook : {}", id);
        return splitBookRepository.findById(id).map(splitBookMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SplitBook : {}", id);
        splitBookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SplitBookDTO> findAllByUser(User user, Pageable pageable) {
        log.debug("Request to get all SplitBooks By User");
        return splitBookRepository.findAllByUser(user, pageable).map(splitBookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SplitBookDTO> findOneByUser(User user, Long id) {
        log.debug("Request to get SplitBook By User: {}", id);
        return splitBookRepository.findOneByUserAndId(user, id).map(splitBookMapper::toDto);
    }

    @Override
    public void deleteByUser(User user, Long id) {
        log.debug("Request to delete SplitBook By User: {}", id);
        splitBookRepository.deleteByUserAndId(user, id);
    }

    @Override
    public SplitBookDTO save(User user, SplitBookDTO splitBookDTO) {
        log.debug("Request to save SplitBook : {}", splitBookDTO);
        SplitBook splitBook = splitBookMapper.toEntity(splitBookDTO);
        splitBook.setUser(user);
        splitBook = splitBookRepository.save(splitBook);
        return splitBookMapper.toDto(splitBook);
    }
}
