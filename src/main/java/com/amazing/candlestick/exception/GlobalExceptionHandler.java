package com.amazing.candlestick.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<?> handleNotFoundDataException(NotFoundDataException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageContainer.builder().errorMessage("DATA NOT FOUND").build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex) {
        log.warn(String.format("Error : %s", ex.getMessage()));
        return ResponseEntity.status(ex.getStatus()).body(ErrorMessageContainer.builder().errorMessage(ex.getReason()).build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn(String.format("Error : %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageContainer.builder().errorMessage("NOT SUPPORTED VALUE").build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGenericExceptions(RuntimeException ex) {
        log.warn(String.format("Error : %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessageContainer.builder().errorMessage(ex.getMessage()).build());
    }

}
