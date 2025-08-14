package com.saym.eventory.shop.application;

import com.saym.eventory.shop.api.dto.ShopRequestDto;
import com.saym.eventory.shop.domain.Shop;
import com.saym.eventory.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    // 가게 생성
    @Transactional
    public Long createShop(ShopRequestDto shopRequestDto) {
        Shop shop = Shop.builder()
                .shopName(shopRequestDto.shopName())
                .shopNum(shopRequestDto.shopNum())
                .shopPlace(shopRequestDto.shopPlace())
                .shopOpenTime(shopRequestDto.shopOpenTime())
                .shopCloseTime(shopRequestDto.shopCloseTime())
                .pictureUrl(shopRequestDto.pictureUrl())
                .build();
        return shopRepository.save(shop).getShopId();

    }

}
