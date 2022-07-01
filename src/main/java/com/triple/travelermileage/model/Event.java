package com.triple.travelermileage.model;

import lombok.Getter;

@Getter
public class Event {

    private EventType type;

    private Action action;

    private String reviewId;

    private String content;

    private String[] attachedPhotoIds;

    private String userId;

    private String placeId;
}
