package com.triple.travelermileage.repository;

import com.triple.travelermileage.model.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewHistoryRepository extends JpaRepository<ReviewHistory, UUID> {
    boolean existsByPlaceId(UUID placeId);
}
