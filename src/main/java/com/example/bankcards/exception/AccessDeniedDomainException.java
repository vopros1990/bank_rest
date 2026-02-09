package com.example.bankcards.exception;

public class AccessDeniedDomainException extends RuntimeException {
    public AccessDeniedDomainException(String message) {
        super(message);
    }
}
