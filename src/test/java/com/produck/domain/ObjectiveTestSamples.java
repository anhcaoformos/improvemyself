package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Objective getObjectiveSample1() {
        return new Objective().id(1L).name("name1");
    }

    public static Objective getObjectiveSample2() {
        return new Objective().id(2L).name("name2");
    }

    public static Objective getObjectiveRandomSampleGenerator() {
        return new Objective().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
