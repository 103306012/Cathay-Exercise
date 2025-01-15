package com.cathay.coindesk.utility;

import java.util.Currency;

public class CurrencyChecker {

    public static boolean isValidCurrencyCode(String code) {
        try {
            Currency currency = Currency.getInstance(code);
            return currency != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
