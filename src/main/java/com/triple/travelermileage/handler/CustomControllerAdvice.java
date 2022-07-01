package com.triple.travelermileage.handler;

import com.triple.travelermileage.response.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Message> argumentExceptionHandler(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new Message(exception.getMessage(), null));
    }
}
