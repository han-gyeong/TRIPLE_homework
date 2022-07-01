package com.triple.travelermileage.response;

import lombok.Getter;

@Getter
public class Message {
    private String message;
    private Object content;

    public Message(String message, Object content) {
        this.message = message;
        this.content = content;
    }
}
