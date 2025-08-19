package com.saym.eventory.store.domain;

import com.saym.eventory.member.domain.Member;
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
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    private String name;
    private String phoneNumber;
    private String pictureUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    private LocalTime breakTimeStart;
    private LocalTime breakTimeEnd;
    private String regularDayOffNote;

    private String address;
    private String addressDetail;
    private String latitude; // 위도
    private String longitude; // 경도

    private String description;
    private String operatingHoursNote;
    private String parkingNote;

    private String couponName;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Store(Member owner, String name, String phoneNumber, String pictureUrl, LocalTime openTime, LocalTime closeTime, LocalTime breakTimeStart, LocalTime breakTimeEnd, String regularDayOffNote, String address, String addressDetail, String latitude, String longitude, String description, String operatingHoursNote, String parkingNote, String couponName) {
        this.owner = owner;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pictureUrl = pictureUrl;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakTimeStart = breakTimeStart;
        this.breakTimeEnd = breakTimeEnd;
        this.regularDayOffNote = regularDayOffNote;
        this.address = address;
        this.addressDetail = addressDetail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.operatingHoursNote = operatingHoursNote;
        this.parkingNote = parkingNote;
        this.couponName = couponName;
    }

    public void updateStore(String name, String phoneNumber, LocalTime openTime, LocalTime closeTime, LocalTime breakTimeStart, LocalTime breakTimeEnd, String regularDayOffNote, String address, String addressDetail, String pictureUrl, String description, String operatingHoursNote, String parkingNote, String couponName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakTimeStart = breakTimeStart;
        this.breakTimeEnd = breakTimeEnd;
        this.regularDayOffNote = regularDayOffNote;
        this.address = address;
        this.addressDetail = addressDetail;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.operatingHoursNote = operatingHoursNote;
        this.parkingNote = parkingNote;
        this.couponName = couponName;
    }
}