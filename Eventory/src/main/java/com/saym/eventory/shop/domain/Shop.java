package com.saym.eventory.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @Column(name = "shop_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_num") // 가게 전화번호
    private Long shopNum;

    @Column(name = "shop_place")
    private String shopPlace;

    @Column(name = "shop_open_time")
    private LocalDate shopOpenTime;

    @Column(name = "shop_close_time")
    private LocalDate shopCloseTime;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Builder
    private Shop(String shopName, Long shopNum, String shopPlace, LocalDate shopOpenTime, LocalDate shopCloseTime, String pictureUrl) {

        this.shopName = shopName;
        this.shopNum = shopNum;
        this.shopPlace = shopPlace;
        this.shopOpenTime = shopOpenTime;
        this.shopCloseTime = shopCloseTime;
        this.pictureUrl = pictureUrl;
    }




}
