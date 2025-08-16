package com.saym.eventory.coupon.api.dto;

import com.saym.eventory.coupon.domain.CouponTimeType;

public record CouponRequestDto(
        String couponName,
        CouponTimeType couponTimeType,
        String couponStartTime,
        String couponEndTime
) {
}
