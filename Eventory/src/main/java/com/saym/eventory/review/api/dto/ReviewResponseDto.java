package com.saym.eventory.review.api.dto;

import com.saym.eventory.review.domain.Review;

public record ReviewResponseDto(
        Long reviewId,
        String reviewContent
) {
    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getReviewContent()
        );
    }
}
