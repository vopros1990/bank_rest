package com.example.bankcards.util;

public class CardMasker {
    public static String mask(String last4) {
        return "**** **** **** " + last4;
    }
}
