package com.produck.domain;

import static com.produck.domain.LedgerTestSamples.*;
import static com.produck.domain.PaymentMethodTestSamples.*;
import static com.produck.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = getPaymentMethodSample1();
        PaymentMethod paymentMethod2 = new PaymentMethod();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);

        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);

        paymentMethod2 = getPaymentMethodSample2();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    void ledgerTest() throws Exception {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        paymentMethod.setLedger(ledgerBack);
        assertThat(paymentMethod.getLedger()).isEqualTo(ledgerBack);

        paymentMethod.ledger(null);
        assertThat(paymentMethod.getLedger()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        paymentMethod.setTransaction(transactionBack);
        assertThat(paymentMethod.getTransaction()).isEqualTo(transactionBack);
        assertThat(transactionBack.getPaymentMethod()).isEqualTo(paymentMethod);

        paymentMethod.transaction(null);
        assertThat(paymentMethod.getTransaction()).isNull();
        assertThat(transactionBack.getPaymentMethod()).isNull();
    }
}
