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

    public static Message ok(Object content) {
        return new Message("정상처리 되었습니다.", content);
    }
}
