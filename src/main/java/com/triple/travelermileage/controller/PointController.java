package com.triple.travelermileage.controller;

import com.triple.travelermileage.response.Message;
import com.triple.travelermileage.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/point/{userId}")
    public ResponseEntity<Message> findPointOfUser(@PathVariable String userId) {
        Integer currentPoint = pointService.getCurrentPoint(userId);
        return ResponseEntity.ok(Message.ok(currentPoint));
    }
}
