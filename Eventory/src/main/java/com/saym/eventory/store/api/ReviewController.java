package com.saym.eventory.store.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.store.api.dto.response.ReviewResponseDto;
import com.saym.eventory.store.application.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "리뷰 관련 API 입니다.")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "가게 리뷰 목록 조회")
    @GetMapping("/review/{storeId}")
    public RspTemplate<List<ReviewResponseDto>> getReviewsByStoreId(@PathVariable Long storeId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByStoreId(storeId);
        return RspTemplate.ok(reviews);
    }
}