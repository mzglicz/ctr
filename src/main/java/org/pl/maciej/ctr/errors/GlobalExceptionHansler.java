package org.pl.maciej.ctr.errors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHansler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolation(ConstraintViolationException exception) {
        var msg = exception.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new Error(msg));
    }

}
