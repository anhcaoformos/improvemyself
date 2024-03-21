package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SplitBookJoinerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SplitBookJoiner getSplitBookJoinerSample1() {
        return new SplitBookJoiner().id(1L).name("name1");
    }

    public static SplitBookJoiner getSplitBookJoinerSample2() {
        return new SplitBookJoiner().id(2L).name("name2");
    }

    public static SplitBookJoiner getSplitBookJoinerRandomSampleGenerator() {
        return new SplitBookJoiner().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
