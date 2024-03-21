package com.produck.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SplitBookDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SplitBookDetail getSplitBookDetailSample1() {
        return new SplitBookDetail().id(1L).description("description1").personName("personName1").groupId("groupId1");
    }

    public static SplitBookDetail getSplitBookDetailSample2() {
        return new SplitBookDetail().id(2L).description("description2").personName("personName2").groupId("groupId2");
    }

    public static SplitBookDetail getSplitBookDetailRandomSampleGenerator() {
        return new SplitBookDetail()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .personName(UUID.randomUUID().toString())
            .groupId(UUID.randomUUID().toString());
    }
}
