package com.saym.eventory.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType; // 쿠폰 타입 (금액 할인, 품목 할인 등)

    private String name; // 쿠폰 이름 ex) 5000원 할인 쿠폰, 음료수 1잔 무료

    private Integer discountAmount; // 할인 금액 (금액 할인 쿠폰인 경우)

    private String discountItem; // 할인 품목 (품목 할인 쿠폰인 경우)

    private LocalDate startDate; // 쿠폰 유효 시작일

    private LocalDate endDate;   // 쿠폰 유효 종료일

    @Builder
    public Coupon(Store store, CouponType couponType, String name, Integer discountAmount, String discountItem, LocalDate startDate, LocalDate endDate) {
        this.store = store;
        this.couponType = couponType;
        this.name = name;
        this.discountAmount = discountAmount;
        this.discountItem = discountItem;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateCoupon(String name, Integer discountAmount, String discountItem, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.discountItem = discountItem;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}