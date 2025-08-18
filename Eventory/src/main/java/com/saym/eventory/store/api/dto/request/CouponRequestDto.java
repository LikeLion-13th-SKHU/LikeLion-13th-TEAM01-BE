package com.saym.eventory.store.api.dto.request;

import com.saym.eventory.store.domain.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record CouponRequestDto(
        @Schema(description = "쿠폰 타입 (AMOUNT_DISCOUNT, ITEM_DISCOUNT)", example = "AMOUNT_DISCOUNT")
        CouponType couponType,

        @Schema(description = "쿠폰 이름 (ex: 5000원 할인 쿠폰)", example = "5000원 할인 쿠폰")
        String name,

        @Schema(description = "할인 금액 (금액 할인 쿠폰인 경우)", example = "5000", nullable = true)
        Integer discountAmount,

        @Schema(description = "할인 품목 (품목 할인 쿠폰인 경우)", example = "아메리카노 1잔 무료", nullable = true)
        String discountItem,

        @Schema(description = "쿠폰 유효 시작일 (YYYY-MM-DD)", example = "2024-01-01")
        LocalDate startDate,

        @Schema(description = "쿠폰 유효 종료일 (YYYY-MM-DD)", example = "2024-12-31")
        LocalDate endDate
) {}
