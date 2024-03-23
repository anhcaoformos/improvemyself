package com.produck.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static User getUserSample1() {
        return new User().id(1L);
    }

    public static User getUserSample2() {
        return new User().id(2L);
    }

    public static User getUserRandomSampleGenerator() {
        return new User().id(longCount.incrementAndGet());
    }
}
