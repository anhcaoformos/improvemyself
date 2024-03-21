package com.produck.domain;

import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.ObjectiveTestSamples.*;
import static com.produck.domain.PaymentCategoryTestSamples.*;
import static com.produck.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCategory.class);
        PaymentCategory paymentCategory1 = getPaymentCategorySample1();
        PaymentCategory paymentCategory2 = new PaymentCategory();
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);

        paymentCategory2.setId(paymentCategory1.getId());
        assertThat(paymentCategory1).isEqualTo(paymentCategory2);

        paymentCategory2 = getPaymentCategorySample2();
        assertThat(paymentCategory1).isNotEqualTo(paymentCategory2);
    }

    @Test
    void ledgerTest() throws Exception {
        PaymentCategory paymentCategory = getPaymentCategoryRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        paymentCategory.setLedger(ledgerBack);
        assertThat(paymentCategory.getLedger()).isEqualTo(ledgerBack);

        paymentCategory.ledger(null);
        assertThat(paymentCategory.getLedger()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        PaymentCategory paymentCategory = getPaymentCategoryRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        paymentCategory.setTransaction(transactionBack);
        assertThat(paymentCategory.getTransaction()).isEqualTo(transactionBack);
        assertThat(transactionBack.getPaymentCategory()).isEqualTo(paymentCategory);

        paymentCategory.transaction(null);
        assertThat(paymentCategory.getTransaction()).isNull();
        assertThat(transactionBack.getPaymentCategory()).isNull();
    }

    @Test
    void objectiveTest() throws Exception {
        PaymentCategory paymentCategory = getPaymentCategoryRandomSampleGenerator();
        Objective objectiveBack = getObjectiveRandomSampleGenerator();

        paymentCategory.setObjective(objectiveBack);
        assertThat(paymentCategory.getObjective()).isEqualTo(objectiveBack);
        assertThat(objectiveBack.getPaymentCategory()).isEqualTo(paymentCategory);

        paymentCategory.objective(null);
        assertThat(paymentCategory.getObjective()).isNull();
        assertThat(objectiveBack.getPaymentCategory()).isNull();
    }
}
