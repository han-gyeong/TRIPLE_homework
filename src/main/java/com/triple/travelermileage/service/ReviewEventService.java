package com.triple.travelermileage.service;

import com.triple.travelermileage.model.Event;
import com.triple.travelermileage.model.ReviewHistory;
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
    private final PointService pointService;
    private final ReviewHistoryRepository reviewHistoryRepository;

    // 리뷰 추가
    public void addReview(Event event) {
        if (isExistReview(event.getReviewId())) {
            throw new IllegalArgumentException("이미 존재하는 리뷰 ID 입니다.");
        }

        if (hasReviewOnPlace(event.getUserId(), event.getPlaceId())) {
            throw new IllegalArgumentException("해당 장소에 이미 리뷰를 작성하였습니다.");
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

        pointService.addPointHistory(event.getUserId(), reviewHistory.getEarnedPoint(), "리뷰 등록으로 인한 지급");
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

            pointService.addPointHistory(event.getUserId(), minusPoint, "리뷰 수정");
        }

        reviewHistory.changeEarnedPoint(calculatePoint);
    }

    // 리뷰 삭제
    public void removeReview(Event event) {
        ReviewHistory reviewHistory = findReviewHistory(event.getReviewId());

        reviewHistoryRepository.deleteById(reviewHistory.getReviewId());
        pointService.addPointHistory(event.getUserId(), reviewHistory.getEarnedPoint() * -1, "리뷰 삭제");
    }

    private int calculatePoint(Event event) {
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
        return !reviewHistoryRepository.existsByPlaceId(UUID.fromString(placeId));
    }

    private boolean isExistReview(String reviewId) {
        return reviewHistoryRepository.findById(UUID.fromString(reviewId)).isPresent();
    }

    private boolean hasReviewOnPlace(String userId, String placeId) {
        return reviewHistoryRepository.existsByUserIdAndPlaceId(UUID.fromString(userId), UUID.fromString(placeId));
    }

    private ReviewHistory findReviewHistory(String reviewId) {
        return reviewHistoryRepository.findById(UUID.fromString(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
    }
}
