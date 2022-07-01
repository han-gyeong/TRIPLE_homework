package com.triple.travelermileage.repository;

import com.triple.travelermileage.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {

    @Query("select sum(m.amount) from PointHistory m where m.userId = ?1")
    Integer getCurrentPointOfUser(UUID userId);
}
