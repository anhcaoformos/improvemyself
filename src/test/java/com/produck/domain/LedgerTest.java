package com.produck.domain;

import static com.produck.domain.GoalTestSamples.*;
import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.ObjectiveTestSamples.*;
import static com.produck.domain.PaymentCategoryTestSamples.*;
import static com.produck.domain.PaymentMethodTestSamples.*;
import static com.produck.domain.TransactionTestSamples.*;
import static com.produck.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LedgerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ledger.class);
        Ledger ledger1 = getLedgerSample1();
        Ledger ledger2 = new Ledger();
        assertThat(ledger1).isNotEqualTo(ledger2);

        ledger2.setId(ledger1.getId());
        assertThat(ledger1).isEqualTo(ledger2);

        ledger2 = getLedgerSample2();
        assertThat(ledger1).isNotEqualTo(ledger2);
    }

    @Test
    void goalTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        Goal goalBack = getGoalRandomSampleGenerator();

        ledger.addGoal(goalBack);
        assertThat(ledger.getGoals()).containsOnly(goalBack);
        assertThat(goalBack.getLedger()).isEqualTo(ledger);

        ledger.removeGoal(goalBack);
        assertThat(ledger.getGoals()).doesNotContain(goalBack);
        assertThat(goalBack.getLedger()).isNull();

        ledger.goals(new HashSet<>(Set.of(goalBack)));
        assertThat(ledger.getGoals()).containsOnly(goalBack);
        assertThat(goalBack.getLedger()).isEqualTo(ledger);

        ledger.setGoals(new HashSet<>());
        assertThat(ledger.getGoals()).doesNotContain(goalBack);
        assertThat(goalBack.getLedger()).isNull();
    }

    @Test
    void objectiveTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        Objective objectiveBack = getObjectiveRandomSampleGenerator();

        ledger.addObjective(objectiveBack);
        assertThat(ledger.getObjectives()).containsOnly(objectiveBack);
        assertThat(objectiveBack.getLedger()).isEqualTo(ledger);

        ledger.removeObjective(objectiveBack);
        assertThat(ledger.getObjectives()).doesNotContain(objectiveBack);
        assertThat(objectiveBack.getLedger()).isNull();

        ledger.objectives(new HashSet<>(Set.of(objectiveBack)));
        assertThat(ledger.getObjectives()).containsOnly(objectiveBack);
        assertThat(objectiveBack.getLedger()).isEqualTo(ledger);

        ledger.setObjectives(new HashSet<>());
        assertThat(ledger.getObjectives()).doesNotContain(objectiveBack);
        assertThat(objectiveBack.getLedger()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        ledger.addTransaction(transactionBack);
        assertThat(ledger.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getLedger()).isEqualTo(ledger);

        ledger.removeTransaction(transactionBack);
        assertThat(ledger.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getLedger()).isNull();

        ledger.transactions(new HashSet<>(Set.of(transactionBack)));
        assertThat(ledger.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getLedger()).isEqualTo(ledger);

        ledger.setTransactions(new HashSet<>());
        assertThat(ledger.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getLedger()).isNull();
    }

    @Test
    void paymentMethodTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        ledger.addPaymentMethod(paymentMethodBack);
        assertThat(ledger.getPaymentMethods()).containsOnly(paymentMethodBack);
        assertThat(paymentMethodBack.getLedger()).isEqualTo(ledger);

        ledger.removePaymentMethod(paymentMethodBack);
        assertThat(ledger.getPaymentMethods()).doesNotContain(paymentMethodBack);
        assertThat(paymentMethodBack.getLedger()).isNull();

        ledger.paymentMethods(new HashSet<>(Set.of(paymentMethodBack)));
        assertThat(ledger.getPaymentMethods()).containsOnly(paymentMethodBack);
        assertThat(paymentMethodBack.getLedger()).isEqualTo(ledger);

        ledger.setPaymentMethods(new HashSet<>());
        assertThat(ledger.getPaymentMethods()).doesNotContain(paymentMethodBack);
        assertThat(paymentMethodBack.getLedger()).isNull();
    }

    @Test
    void paymentCategoryTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        PaymentCategory paymentCategoryBack = getPaymentCategoryRandomSampleGenerator();

        ledger.addPaymentCategory(paymentCategoryBack);
        assertThat(ledger.getPaymentCategories()).containsOnly(paymentCategoryBack);
        assertThat(paymentCategoryBack.getLedger()).isEqualTo(ledger);

        ledger.removePaymentCategory(paymentCategoryBack);
        assertThat(ledger.getPaymentCategories()).doesNotContain(paymentCategoryBack);
        assertThat(paymentCategoryBack.getLedger()).isNull();

        ledger.paymentCategories(new HashSet<>(Set.of(paymentCategoryBack)));
        assertThat(ledger.getPaymentCategories()).containsOnly(paymentCategoryBack);
        assertThat(paymentCategoryBack.getLedger()).isEqualTo(ledger);

        ledger.setPaymentCategories(new HashSet<>());
        assertThat(ledger.getPaymentCategories()).doesNotContain(paymentCategoryBack);
        assertThat(paymentCategoryBack.getLedger()).isNull();
    }

    @Test
    void userTest() throws Exception {
        Ledger ledger = getLedgerRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        ledger.setUser(userBack);
        assertThat(ledger.getUser()).isEqualTo(userBack);

        ledger.user(null);
        assertThat(ledger.getUser()).isNull();
    }
}
