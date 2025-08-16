package com.saym.eventory.shop.application;

import com.saym.eventory.coupon.api.dto.CouponRequestDto;
import com.saym.eventory.coupon.domain.Coupon;
import com.saym.eventory.coupon.domain.CouponTimeType;
import com.saym.eventory.menu.api.dto.MenuRequestDto;
import com.saym.eventory.menu.domain.Menu;
import com.saym.eventory.shop.api.dto.ShopRequestDto;
import com.saym.eventory.shop.api.dto.ShopResponseDto;
import com.saym.eventory.shop.domain.Shop;
import com.saym.eventory.shop.domain.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    // 가게 등록
    @Transactional
    public Long createShop(ShopRequestDto shopRequestDto) {
        Shop shop = Shop.builder()
                .shopName(shopRequestDto.shopName())
                .shopNum(shopRequestDto.shopNum())
                .shopPlace(shopRequestDto.shopPlace())
                .shopOpenTime(shopRequestDto.shopOpenTime())
                .shopCloseTime(shopRequestDto.shopCloseTime())
                .pictureUrl(shopRequestDto.pictureUrl())
                .startBreakTime(shopRequestDto.startBreakTime())
                .endBreakTime(shopRequestDto.endBreakTime())
                .dayOff(shopRequestDto.dayOff())
                .build();

        // 메뉴 추가
        if (shopRequestDto.menus() != null) {
            for (MenuRequestDto menuRequestDto : shopRequestDto.menus()) {
                Menu menu = Menu.builder()
                        .menuName(menuRequestDto.menuName())
                        .menuPrice(menuRequestDto.menuPrice())
                        .build();
                shop.addMenu(menu);
            }
        }

        // 쿠폰 추가
        if (shopRequestDto.coupons() != null) {
            for (CouponRequestDto couponRequestDto : shopRequestDto.coupons()) {
                Coupon coupon = Coupon.builder()
                        .couponName(couponRequestDto.couponName())
                        .couponTimeType(couponRequestDto.couponTimeType())
                        .couponStartTime(couponRequestDto.couponTimeType() == CouponTimeType.LIMITED ? couponRequestDto.couponStartTime() : null)
                        .couponEndTime(couponRequestDto.couponTimeType() == CouponTimeType.LIMITED ? couponRequestDto.couponEndTime() : null)
                        .build();
                shop.addCoupon(coupon);
            }
        }

        return shopRepository.save(shop).getShopId();

    }

    // 가게 수정
    @Transactional
    public void updateShop(Long shopId, ShopRequestDto dto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        shop.updateShop(dto);

        // 기존 메뉴 초기화 후 추가
        shop.clearMenus();
        if (dto.menus() != null) {
            for (MenuRequestDto m : dto.menus()) {
                Menu menu = Menu.builder()
                        .menuName(m.menuName())
                        .menuPrice(m.menuPrice())
                        .build();
                shop.addMenu(menu);
            }
        }

        // 기존 쿠폰 초기화 후 추가
        shop.clearCoupons();
        if (dto.coupons() != null) {
            for (CouponRequestDto c : dto.coupons()) {
                Coupon coupon = Coupon.builder()
                        .couponName(c.couponName())
                        .couponTimeType(c.couponTimeType())
                        .couponStartTime(c.couponTimeType() == CouponTimeType.LIMITED ? c.couponStartTime() : null)
                        .couponEndTime(c.couponTimeType() == CouponTimeType.LIMITED ? c.couponEndTime() : null)
                        .build();
                shop.addCoupon(coupon);
            }
        }

    }

    // 가게 삭제
    @Transactional
    public void deleteShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException(("가게를 찾을 수 없습니다.")));
        shopRepository.delete(shop);
    }

    // 가게 상세 조회 (메뉴, 쿠폰, 리뷰 포함)
    public ShopResponseDto getShopDetail(Long shopId) {
        Shop shop = shopRepository.findShopDetailById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        return ShopResponseDto.from(shop);
    }

}
