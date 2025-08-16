package com.saym.eventory.shop.domain;


import com.saym.eventory.coupon.domain.Coupon;
import com.saym.eventory.menu.domain.Menu;
import com.saym.eventory.review.domain.Review;
import com.saym.eventory.shop.api.dto.ShopRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @Column(name = "shop_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "shop_num") // 가게 전화번호
    private Long shopNum;

    @Column(name = "shop_place", nullable = false)
    private String shopPlace;

    @Column(name = "shop_open_time", nullable = false)
    private LocalTime shopOpenTime;

    @Column(name = "shop_close_time", nullable = false)
    private LocalTime shopCloseTime;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "start_Break_time")
    private LocalTime startBreakTime;

    @Column(name = "end_Break_time")
    private LocalTime endBreakTime;

    @Column(name = "day_off")
    private String dayOff;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    private Shop(String shopName, Long shopNum, String shopPlace, LocalTime shopOpenTime, LocalTime shopCloseTime, String pictureUrl, LocalTime startBreakTime, LocalTime endBreakTime, String dayOff) {

        this.shopName = shopName;
        this.shopNum = shopNum;
        this.shopPlace = shopPlace;
        this.shopOpenTime = shopOpenTime;
        this.shopCloseTime = shopCloseTime;
        this.pictureUrl = pictureUrl;
        this.startBreakTime = startBreakTime;
        this.endBreakTime = endBreakTime;
        this.dayOff = dayOff;
    }

    public void updateShop(ShopRequestDto shopRequestDto) {
        this.shopName = shopRequestDto.shopName();
        this.shopNum = shopRequestDto.shopNum();
        this.shopPlace = shopRequestDto.shopPlace();
        this.shopOpenTime = shopRequestDto.shopOpenTime();
        this.shopCloseTime = shopRequestDto.shopCloseTime();
        this.pictureUrl = shopRequestDto.pictureUrl();
        this.startBreakTime = shopRequestDto.startBreakTime();
        this.endBreakTime = shopRequestDto.endBreakTime();
        this.dayOff = shopRequestDto.dayOff();
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.assignShop(this);
    }

    public void clearMenus() {
        menus.clear();
    }

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
        coupon.assignShop(this);
    }

    public void clearCoupons() {
        for (Coupon coupon : coupons) {
            coupon.assignShop(null);
        }
        coupons.clear();
    }

}
