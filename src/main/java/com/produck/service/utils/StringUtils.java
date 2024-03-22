package com.produck.service.utils;

import java.util.Base64;
import org.springframework.data.util.Pair;

/**
 * Utility class for String.
 */
public final class StringUtils {

    private StringUtils() {}

    public static String encodeSplitBookKey(Long userId, Long splitBookId) {
        return Base64.getEncoder().encodeToString((userId + "_" + splitBookId).getBytes());
    }

    public static Pair<Long, Long> decodeSplitBookKey(String key) {
        String decodeString = new String(Base64.getDecoder().decode(key));
        String[] ids = decodeString.split("_");
        Long userId = Long.parseLong(ids[0]);
        Long splitBookId = Long.parseLong(ids[1]);
        return Pair.of(userId, splitBookId);
    }
}
