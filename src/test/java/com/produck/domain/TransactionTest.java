package com.produck.domain;

import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.ObjectiveTestSamples.*;
import static com.produck.domain.PaymentCategoryTestSamples.*;
import static com.produck.domain.PaymentMethodTestSamples.*;
import static com.produck.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void objectiveTest() throws Exception {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Objective objectiveBack = getObjectiveRandomSampleGenerator();

        transaction.setObjective(objectiveBack);
        assertThat(transaction.getObjective()).isEqualTo(objectiveBack);

        transaction.objective(null);
        assertThat(transaction.getObjective()).isNull();
    }

    @Test
    void paymentMethodTest() throws Exception {
        Transaction transaction = getTransactionRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        transaction.setPaymentMethod(paymentMethodBack);
        assertThat(transaction.getPaymentMethod()).isEqualTo(paymentMethodBack);

        transaction.paymentMethod(null);
        assertThat(transaction.getPaymentMethod()).isNull();
    }

    @Test
    void paymentCategoryTest() throws Exception {
        Transaction transaction = getTransactionRandomSampleGenerator();
        PaymentCategory paymentCategoryBack = getPaymentCategoryRandomSampleGenerator();

        transaction.setPaymentCategory(paymentCategoryBack);
        assertThat(transaction.getPaymentCategory()).isEqualTo(paymentCategoryBack);

        transaction.paymentCategory(null);
        assertThat(transaction.getPaymentCategory()).isNull();
    }

    @Test
    void ledgerTest() throws Exception {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        transaction.setLedger(ledgerBack);
        assertThat(transaction.getLedger()).isEqualTo(ledgerBack);

        transaction.ledger(null);
        assertThat(transaction.getLedger()).isNull();
    }
}
