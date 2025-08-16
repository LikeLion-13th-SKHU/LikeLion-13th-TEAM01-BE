package com.saym.eventory.review.api.dto;

public record ReviewRequestDto(
        Long reviewId,
        String reviewContent
) {
}