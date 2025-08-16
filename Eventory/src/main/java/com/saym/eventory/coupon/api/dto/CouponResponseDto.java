package com.saym.eventory.coupon.api.dto;

import com.saym.eventory.coupon.domain.Coupon;
import com.saym.eventory.coupon.domain.CouponTimeType;

import java.time.LocalDateTime;

public record CouponResponseDto(
        Long couponId,
        String couponName,
        CouponTimeType couponTimeType,
        String couponStartTime,
        String couponEndTime
) {
    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getCouponId(),
                coupon.getCouponName(),
                coupon.getCouponTimeType(),
                coupon.getCouponStartTime(),
                coupon.getCouponEndTime()
        );
    }
}