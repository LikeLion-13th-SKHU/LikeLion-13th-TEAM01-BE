package com.saym.eventory.shop.api.dto;

import java.time.LocalDate;

public record ShopRequestDto(
        String shopName,
        Long shopNum,
        String shopPlace,
        LocalDate shopOpenTime,
        LocalDate shopCloseTime,
        String pictureUrl
) {
}
