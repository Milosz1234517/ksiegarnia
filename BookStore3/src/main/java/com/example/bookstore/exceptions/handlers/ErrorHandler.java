package com.example.bookstore.exceptions.handlers;

import com.example.bookstore.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getBindingResult().getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .filter(Objects::nonNull)
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException ex) {
        final Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception e) {
        final Map<String, Object> body = new HashMap<>();
        log.error(e.getMessage());
        body.put("message", "Something went wrong, no access to resources");
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        final Map<String, Object> body = new HashMap<>();
        log.error(e.getMessage());
        body.put("message", "Something went wrong, no access to resources");
        return ResponseEntity.badRequest().body(body);
    }

}
