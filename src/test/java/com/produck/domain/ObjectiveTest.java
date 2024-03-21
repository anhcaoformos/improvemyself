package com.produck.domain;

import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.ObjectiveTestSamples.*;
import static com.produck.domain.PaymentCategoryTestSamples.*;
import static com.produck.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjectiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objective.class);
        Objective objective1 = getObjectiveSample1();
        Objective objective2 = new Objective();
        assertThat(objective1).isNotEqualTo(objective2);

        objective2.setId(objective1.getId());
        assertThat(objective1).isEqualTo(objective2);

        objective2 = getObjectiveSample2();
        assertThat(objective1).isNotEqualTo(objective2);
    }

    @Test
    void paymentCategoryTest() throws Exception {
        Objective objective = getObjectiveRandomSampleGenerator();
        PaymentCategory paymentCategoryBack = getPaymentCategoryRandomSampleGenerator();

        objective.setPaymentCategory(paymentCategoryBack);
        assertThat(objective.getPaymentCategory()).isEqualTo(paymentCategoryBack);

        objective.paymentCategory(null);
        assertThat(objective.getPaymentCategory()).isNull();
    }

    @Test
    void ledgerTest() throws Exception {
        Objective objective = getObjectiveRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        objective.setLedger(ledgerBack);
        assertThat(objective.getLedger()).isEqualTo(ledgerBack);

        objective.ledger(null);
        assertThat(objective.getLedger()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        Objective objective = getObjectiveRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        objective.setTransaction(transactionBack);
        assertThat(objective.getTransaction()).isEqualTo(transactionBack);
        assertThat(transactionBack.getObjective()).isEqualTo(objective);

        objective.transaction(null);
        assertThat(objective.getTransaction()).isNull();
        assertThat(transactionBack.getObjective()).isNull();
    }
}
