package com.produck.service.impl;

import com.produck.domain.Ledger;
import com.produck.repository.LedgerRepository;
import com.produck.service.LedgerService;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.mapper.LedgerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.produck.domain.Ledger}.
 */
@Service
@Transactional
public class LedgerServiceImpl implements LedgerService {

    private final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);

    private final LedgerRepository ledgerRepository;

    private final LedgerMapper ledgerMapper;

    public LedgerServiceImpl(LedgerRepository ledgerRepository, LedgerMapper ledgerMapper) {
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
    }

    @Override
    public LedgerDTO save(LedgerDTO ledgerDTO) {
        log.debug("Request to save Ledger : {}", ledgerDTO);
        Ledger ledger = ledgerMapper.toEntity(ledgerDTO);
        ledger = ledgerRepository.save(ledger);
        return ledgerMapper.toDto(ledger);
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
    public void delete(Long id) {
        log.debug("Request to delete Ledger : {}", id);
        ledgerRepository.deleteById(id);
    }
}
