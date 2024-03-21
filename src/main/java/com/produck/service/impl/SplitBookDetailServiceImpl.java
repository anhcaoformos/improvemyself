package com.produck.service.impl;

import com.produck.domain.SplitBookDetail;
import com.produck.repository.SplitBookDetailRepository;
import com.produck.service.SplitBookDetailService;
import com.produck.service.dto.SplitBookDetailDTO;
import com.produck.service.mapper.SplitBookDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.produck.domain.SplitBookDetail}.
 */
@Service
@Transactional
public class SplitBookDetailServiceImpl implements SplitBookDetailService {

    private final Logger log = LoggerFactory.getLogger(SplitBookDetailServiceImpl.class);

    private final SplitBookDetailRepository splitBookDetailRepository;

    private final SplitBookDetailMapper splitBookDetailMapper;

    public SplitBookDetailServiceImpl(SplitBookDetailRepository splitBookDetailRepository, SplitBookDetailMapper splitBookDetailMapper) {
        this.splitBookDetailRepository = splitBookDetailRepository;
        this.splitBookDetailMapper = splitBookDetailMapper;
    }

    @Override
    public SplitBookDetailDTO save(SplitBookDetailDTO splitBookDetailDTO) {
        log.debug("Request to save SplitBookDetail : {}", splitBookDetailDTO);
        SplitBookDetail splitBookDetail = splitBookDetailMapper.toEntity(splitBookDetailDTO);
        splitBookDetail = splitBookDetailRepository.save(splitBookDetail);
        return splitBookDetailMapper.toDto(splitBookDetail);
    }

    @Override
    public SplitBookDetailDTO update(SplitBookDetailDTO splitBookDetailDTO) {
        log.debug("Request to update SplitBookDetail : {}", splitBookDetailDTO);
        SplitBookDetail splitBookDetail = splitBookDetailMapper.toEntity(splitBookDetailDTO);
        splitBookDetail = splitBookDetailRepository.save(splitBookDetail);
        return splitBookDetailMapper.toDto(splitBookDetail);
    }

    @Override
    public Optional<SplitBookDetailDTO> partialUpdate(SplitBookDetailDTO splitBookDetailDTO) {
        log.debug("Request to partially update SplitBookDetail : {}", splitBookDetailDTO);

        return splitBookDetailRepository
            .findById(splitBookDetailDTO.getId())
            .map(existingSplitBookDetail -> {
                splitBookDetailMapper.partialUpdate(existingSplitBookDetail, splitBookDetailDTO);

                return existingSplitBookDetail;
            })
            .map(splitBookDetailRepository::save)
            .map(splitBookDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SplitBookDetailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SplitBookDetails");
        return splitBookDetailRepository.findAll(pageable).map(splitBookDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SplitBookDetailDTO> findOne(Long id) {
        log.debug("Request to get SplitBookDetail : {}", id);
        return splitBookDetailRepository.findById(id).map(splitBookDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SplitBookDetail : {}", id);
        splitBookDetailRepository.deleteById(id);
    }
}
