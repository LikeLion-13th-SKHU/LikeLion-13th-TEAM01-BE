package com.saym.eventory.store.api.dto.response;

import com.saym.eventory.store.domain.Coupon;
import com.saym.eventory.store.domain.CouponType;

import java.time.LocalDate;

// 쿠폰 응답 DTO
public record CouponResponseDto(
        Long id,
        Long storeId,
        CouponType couponType,
        String name,
        Integer discountAmount,
        String discountItem,
        LocalDate startDate,
        LocalDate endDate
) {
    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getStore().getId(),
                coupon.getCouponType(),
                coupon.getName(),
                coupon.getDiscountAmount(),
                coupon.getDiscountItem(),
                coupon.getStartDate(),
                coupon.getEndDate()
        );
    }
}