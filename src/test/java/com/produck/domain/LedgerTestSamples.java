package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LedgerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ledger getLedgerSample1() {
        return new Ledger().id(1L).name("name1");
    }

    public static Ledger getLedgerSample2() {
        return new Ledger().id(2L).name("name2");
    }

    public static Ledger getLedgerRandomSampleGenerator() {
        return new Ledger().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
