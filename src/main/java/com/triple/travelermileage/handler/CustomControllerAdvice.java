package com.triple.travelermileage.handler;

import com.triple.travelermileage.response.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Message> argumentExceptionHandler(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new Message(exception.getMessage(), null));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Message> notReadableException() {
        return ResponseEntity.badRequest().body(new Message("TYPE이나 ACTION 이 잘못 입력되었습니다.", null));
    }
}
