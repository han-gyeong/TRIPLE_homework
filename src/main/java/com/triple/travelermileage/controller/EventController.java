package com.triple.travelermileage.controller;

import com.triple.travelermileage.model.Action;
import com.triple.travelermileage.model.Event;
import com.triple.travelermileage.response.Message;
import com.triple.travelermileage.service.ReviewEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final ReviewEventService reviewEventService;

    @PostMapping("/events")
    public ResponseEntity<Message> reviewEvent(@RequestBody Event event) {
        if (event.getAction() == Action.ADD) {
            reviewEventService.addReview(event);
        } else if (event.getAction() == Action.DELETE) {
            reviewEventService.removeReview(event);
        } else if (event.getAction() == Action.MOD) {
            reviewEventService.editReview(event);
        }

        return ResponseEntity.ok(Message.ok(null));
    }
}
