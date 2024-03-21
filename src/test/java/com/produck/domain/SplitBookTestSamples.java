package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SplitBookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SplitBook getSplitBookSample1() {
        return new SplitBook().id(1L).description("description1").name("name1");
    }

    public static SplitBook getSplitBookSample2() {
        return new SplitBook().id(2L).description("description2").name("name2");
    }

    public static SplitBook getSplitBookRandomSampleGenerator() {
        return new SplitBook().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
