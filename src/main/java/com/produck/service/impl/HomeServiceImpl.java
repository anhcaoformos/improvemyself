package com.produck.service.impl;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.service.*;
import com.produck.service.dto.HomeDTO;
import com.produck.service.mapper.LedgerMapper;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Hoe.
 */
@Service
@Transactional
public class HomeServiceImpl implements HomeService {

    private final Logger log = LoggerFactory.getLogger(HomeServiceImpl.class);

    private final LedgerService ledgerService;

    private final LedgerMapper ledgerMapper;

    private final TransactionService transactionService;

    private final PaymentMethodService paymentMethodService;

    private final PaymentCategoryService paymentCategoryService;

    public HomeServiceImpl(
        LedgerService ledgerService,
        LedgerMapper ledgerMapper,
        TransactionService transactionService,
        PaymentMethodService paymentMethodService,
        PaymentCategoryService paymentCategoryService
    ) {
        this.ledgerMapper = ledgerMapper;
        this.transactionService = transactionService;
        this.ledgerService = ledgerService;
        this.paymentMethodService = paymentMethodService;
        this.paymentCategoryService = paymentCategoryService;
    }

    @Override
    public HomeDTO getHomeDashboard(User user, Ledger selectedLedger) {
        HomeDTO home = new HomeDTO();
        ledgerService.setDefault(user, selectedLedger.getId());
        home.setLedger(ledgerMapper.toDto(selectedLedger));
        home.setLedgers(ledgerService.findAllByUser(user));
        home.setTransactions(transactionService.findAllByLedger(selectedLedger, PageRequest.ofSize(20)));
        home.setFilterTransactionDates(transactionService.getFilterTransactionDates(selectedLedger));
        home.setAssetBalances(transactionService.findAllAssetBalanceByLedger(selectedLedger, PageRequest.ofSize(20)));
        home.setObjectivePayBooks(transactionService.findAllObjectivePayBookByLedger(selectedLedger, PageRequest.ofSize(20)));
        home.setSummaryPaymentCategories(transactionService.findSummaryPaymentCategories(selectedLedger, LocalDate.now()));
        home.setPaymentMethods(paymentMethodService.findAllByLedger(selectedLedger, PageRequest.ofSize(20)));
        home.setExistingPaymentCategories(paymentCategoryService.findAllExistingByLedger(selectedLedger));
        home.setExistingPaymentMethods(paymentMethodService.findAllExistingByLedger(selectedLedger));
        return home;
    }
}
