package com.saym.eventory.coupon.domain;

import com.saym.eventory.shop.domain.Shop;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @Column(name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Column(name = "coupon_start_time")
    private String couponStartTime;

    @Column(name = "coupon_end_time")
    private String couponEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "couopon_time_type")
    private CouponTimeType couponTimeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopId")
    private Shop shop;

    @Builder
    public Coupon(String couponName, String couponStartTime, String couponEndTime, CouponTimeType couponTimeType, Shop shop) {
        this.couponName = couponName;
        this.couponStartTime = couponStartTime;
        this.couponEndTime = couponEndTime;
        this.couponTimeType = couponTimeType;
        this.shop = shop;
    }
    public void assignShop(Shop shop) {
        this.shop = shop;
    }

}
