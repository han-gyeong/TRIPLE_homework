package com.triple.travelermileage.repository;

import com.triple.travelermileage.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
}
