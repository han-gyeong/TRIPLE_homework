package com.triple.travelermileage.service;

import com.triple.travelermileage.model.PointHistory;
import com.triple.travelermileage.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;

    public Integer getCurrentPoint(String userId) {
        Integer currentPointOfUser = pointHistoryRepository.getCurrentPointOfUser(UUID.fromString(userId));
        return currentPointOfUser == null ? 0 : currentPointOfUser;
    }

    public void addPointHistory(String userId, int amount, String comment) {
        PointHistory pointHistory = PointHistory.builder()
                .id(UUID.randomUUID())
                .userId(UUID.fromString(userId))
                .amount(amount)
                .comment(comment)
                .build();

        pointHistoryRepository.save(pointHistory);
    }
}
