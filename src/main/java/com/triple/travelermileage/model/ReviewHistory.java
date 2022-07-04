package com.triple.travelermileage.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewHistory {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer earnedPoint;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID placeId;

    @Column(nullable = false)
    @Accessors(fluent = true)
    private boolean hasContent;

    @Column(nullable = false)
    @Accessors(fluent = true)
    private boolean hasPhoto;

    @Column(nullable = false)
    @Accessors(fluent = true)
    private boolean isFirst;

    public void changeEarnedPoint(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("얻은 포인트를 음수로 바꿀 수 없습니다.");
        }

        this.earnedPoint = value;
    }
}