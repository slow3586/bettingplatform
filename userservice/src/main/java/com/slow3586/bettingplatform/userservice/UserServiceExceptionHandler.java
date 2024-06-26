package com.slow3586.bettingplatform.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
@ControllerAdvice
public class UserServiceExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity exception(Throwable e) {
        log.error("#exception handler", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ResponseEntity undeclared(Throwable e) {
        log.error("#exception handler", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Bad arguments: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDeniedException(AccessDeniedException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("Unauthorized: " + e.getMessage());
    }
}
