package com.example.bankcards.exception;

import com.example.bankcards.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistsException(AlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        return messages.size() == 1 ? new ErrorResponse(messages.get(0)) : new ErrorResponse(messages);
    }

    @ExceptionHandler({AccessDeniedDomainException.class, UserBlockedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({CardExpiredException.class, CardExpiredException.class, CardBalanceException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCardExceptions(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenExceptions(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
