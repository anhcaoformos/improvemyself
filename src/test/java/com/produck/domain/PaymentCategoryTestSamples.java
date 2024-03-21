package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentCategory getPaymentCategorySample1() {
        return new PaymentCategory().id(1L).code("code1").name("name1");
    }

    public static PaymentCategory getPaymentCategorySample2() {
        return new PaymentCategory().id(2L).code("code2").name("name2");
    }

    public static PaymentCategory getPaymentCategoryRandomSampleGenerator() {
        return new PaymentCategory().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
