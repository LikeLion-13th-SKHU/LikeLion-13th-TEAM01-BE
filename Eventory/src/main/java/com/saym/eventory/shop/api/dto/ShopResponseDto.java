package com.saym.eventory.shop.api.dto;

import com.saym.eventory.coupon.api.dto.CouponResponseDto;
import com.saym.eventory.menu.api.dto.MenuResponseDto;
import com.saym.eventory.review.api.dto.ReviewResponseDto;
import com.saym.eventory.shop.domain.Shop;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public record ShopResponseDto(
        Long shopId,
        String shopName,
        Long shopNum,
        String shopPlace,
        LocalTime shopOpenTime,
        LocalTime shopCloseTime,
        String pictureUrl,
        LocalTime startBreakTime,
        LocalTime endBreakTime,
        String dayOff,
        List<MenuResponseDto> menus,
        List<CouponResponseDto> coupons,
        List<ReviewResponseDto> reviews
) {
    public static ShopResponseDto from(Shop shop) {
        List<MenuResponseDto> menuDtos = shop.getMenus().stream()
                .map(MenuResponseDto::from)
                .collect(Collectors.toList());

        List<CouponResponseDto> couponDtos = shop.getCoupons().stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());

        List<ReviewResponseDto> reviewDtos = shop.getReviews().stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());

        return new ShopResponseDto(
                shop.getShopId(),
                shop.getShopName(),
                shop.getShopNum(),
                shop.getShopPlace(),
                shop.getShopOpenTime(),
                shop.getShopCloseTime(),
                shop.getPictureUrl(),
                shop.getStartBreakTime(),
                shop.getEndBreakTime(),
                shop.getDayOff(),
                menuDtos,
                couponDtos,
                reviewDtos
        );
    }
}
