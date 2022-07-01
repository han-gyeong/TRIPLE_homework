package com.triple.travelermileage.service;

import com.triple.travelermileage.model.Event;
import com.triple.travelermileage.model.PointHistory;
import com.triple.travelermileage.model.ReviewHistory;
import com.triple.travelermileage.repository.PointHistoryRepository;
import com.triple.travelermileage.repository.ReviewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewEventService {

    private final PointHistoryRepository pointHistoryRepository;
    private final ReviewHistoryRepository reviewHistoryRepository;

    // 리뷰 추가
    public void addReview(Event event) {
        if (isExistReview(event.getReviewId())) {
            throw new IllegalArgumentException("이미 존재하는 리뷰 ID 입니다.");
        }

        ReviewHistory reviewHistory = ReviewHistory.builder()
                .userId(UUID.fromString(event.getUserId()))
                .earnedPoint(calculatePoint(event))
                .reviewId(UUID.fromString(event.getReviewId()))
                .placeId(UUID.fromString(event.getPlaceId()))
                .hasContent(StringUtils.hasText(event.getContent()))
                .hasPhoto(event.getAttachedPhotoIds().length > 0)
                .isFirst(isFirstReview(event.getPlaceId()))
                .build();

        PointHistory pointHistory = PointHistory.builder()
                .id(UUID.randomUUID())
                .amount(reviewHistory.getEarnedPoint())
                .userId(reviewHistory.getUserId())
                .comment("리뷰 등록으로 인한 지급")
                .build();

        pointHistoryRepository.save(pointHistory);
        reviewHistoryRepository.save(reviewHistory);
    }

    // 리뷰 수정
    public void editReview(Event event) {
        ReviewHistory reviewHistory = findReviewHistory(event.getReviewId());

        int calculatePoint = calculatePoint(event);

        // 첫 게시자가 수정시 첫 게시물에 해당되지 않는 문제 해결용
        if (reviewHistory.isFirst()) {
            calculatePoint++;
        }

        if (!reviewHistory.getEarnedPoint().equals(calculatePoint)) {
            int minusPoint = calculatePoint - reviewHistory.getEarnedPoint();

            PointHistory pointHistory = PointHistory.builder()
                    .id(UUID.randomUUID())
                    .amount(minusPoint)
                    .userId(reviewHistory.getUserId())
                    .comment("리뷰 수정")
                    .build();

            pointHistoryRepository.save(pointHistory);
        }

        reviewHistory.changeEarnedPoint(calculatePoint);
    }

    // 리뷰 삭제
    public void removeReview(Event event) {
        ReviewHistory reviewHistory = findReviewHistory(event.getReviewId());

        reviewHistoryRepository.deleteById(reviewHistory.getReviewId());

        PointHistory pointHistory = PointHistory.builder()
                .id(UUID.randomUUID())
                .amount(reviewHistory.getEarnedPoint() * -1)
                .userId(reviewHistory.getUserId())
                .comment("리뷰 삭제")
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    private Integer calculatePoint(Event event) {
        int score = 0;

        if (StringUtils.hasText(event.getContent())) {
            score++;
        }

        if (event.getAttachedPhotoIds().length > 0) {
            score++;
        }

        if (isFirstReview(event.getPlaceId())) {
            score++;
        }

        return score;
    }

    // 첫번째 리뷰인지, 리뷰에 대한 히스토리에서 해당 PlaceId가 없을 경우
    private boolean isFirstReview(String placeId) {
        if (reviewHistoryRepository.existsByPlaceId(UUID.fromString(placeId))) {
            return false;
        }

        return true;
    }

    private boolean isExistReview(String reviewId) {
        return reviewHistoryRepository.findById(UUID.fromString(reviewId)).isPresent();
    }

    private ReviewHistory findReviewHistory(String reviewId) {
        return reviewHistoryRepository.findById(UUID.fromString(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
    }
}
