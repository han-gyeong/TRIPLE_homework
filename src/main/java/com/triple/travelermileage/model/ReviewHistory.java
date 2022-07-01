package com.triple.travelermileage.model;

import lombok.*;

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

    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private Integer earnedPoint;

    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    private boolean hasContent;

    private boolean hasPhoto;
}