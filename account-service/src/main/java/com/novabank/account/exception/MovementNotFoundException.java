package com.novabank.account.exception;

public class MovementNotFoundException extends RuntimeException {
    public MovementNotFoundException(String message) {
        super(message);
    }
}
