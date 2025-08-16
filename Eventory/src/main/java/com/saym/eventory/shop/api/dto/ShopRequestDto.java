package com.saym.eventory.shop.api.dto;

import com.saym.eventory.coupon.api.dto.CouponRequestDto;
import com.saym.eventory.menu.api.dto.MenuRequestDto;
import com.saym.eventory.review.api.dto.ReviewRequestDto;

import java.time.LocalTime;
import java.util.List;

public record ShopRequestDto(
        String shopName,
        Long shopNum,
        String shopPlace,
        LocalTime shopOpenTime,
        LocalTime shopCloseTime,
        String pictureUrl,
        LocalTime startBreakTime,
        LocalTime endBreakTime,
        String dayOff,
        List<MenuRequestDto> menus,
        List<CouponRequestDto> coupons
) {
}
