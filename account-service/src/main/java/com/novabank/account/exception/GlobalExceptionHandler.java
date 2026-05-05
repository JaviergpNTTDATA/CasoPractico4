package com.novabank.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    //Client not found 404
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClienteNotFound(ClientNotFoundException ex) {
        return buildResponse("CLIENT_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    
    //Movement not found 404
    @ExceptionHandler(MovementNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMovementNotFound(MovementNotFoundException ex) {
        return buildResponse("MOVEMENT_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /* 
    //User not found 404
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(
                "INVALID_CREDENTIALS",
                "User or password incorrect",
                HttpStatus.UNAUTHORIZED
        );
    }*/

    //Account not found 404
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCuentaNotFound(AccountNotFoundException ex) {
        return buildResponse("ACCOUNT_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    
    //insufficient balance 422
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleSaldoInsuficiente(InsufficientBalanceException ex) {
        return buildResponse("INSUFFICIENT_BALANCE", ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    //Invdalid or duplicated error 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse("BAD_REQUEST", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //Validation error 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_ERROR");
        body.put("messages", errores);
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //Method to build responses
    private ResponseEntity<Map<String, Object>> buildResponse(String errorCode, String message, HttpStatus status) {

        Map<String, Object> body = new HashMap<>();
        body.put("error", errorCode);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, status);
    }
}