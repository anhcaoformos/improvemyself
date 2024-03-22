package com.produck.config;

import com.produck.domain.enumeration.PaymentCategoryCode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String DEFAULT = "Default";

    public static final String OWNER = "Owner";

    public static final List<String> INCOME_PAY_BOOK_PAYMENT_CATEGORIES = Arrays.asList(
        PaymentCategoryCode.BORROW.name(),
        PaymentCategoryCode.RECEIVED.name()
    );

    public static final List<String> EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES = Arrays.asList(
        PaymentCategoryCode.LOAN.name(),
        PaymentCategoryCode.PAID.name()
    );

    public static final List<String> PAY_BOOK_PAYMENT_CATEGORIES = Stream.concat(
        INCOME_PAY_BOOK_PAYMENT_CATEGORIES.stream(),
        EXPENSE_PAY_BOOK_PAYMENT_CATEGORIES.stream()
    ).collect(Collectors.toList());

    private Constants() {}
}
