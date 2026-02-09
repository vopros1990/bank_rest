package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

import java.time.YearMonth;

@UtilityClass
public class ExpiryChecker {
    public static boolean isExpired(int year, int month) {
        YearMonth now = YearMonth.now();

        YearMonth check = YearMonth.of(year, month);

        return now.isAfter(check);
    }
}
