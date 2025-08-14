package com.saym.eventory.event.domain;

public enum Area {
    SEOUL,
    INCHEON,
    BUSAN,
    DAEGU,
    DAEJEON,
    GWANGJU,
    ULSAN,
    SEJONG,
    GYEONGGI,
    GANGWON,
    CHUNGBUK,
    CHUNGNAM,
    JEONBUK,
    JEONNAM,
    GYEONGBUK,
    GYEONGNAM,
    JEJU;

    // 대소문자 구분 없이 enum 반환
    public static Area fromString(String value) {
        if (value == null) return null;

        return java.util.Arrays.stream(Area.values())
                .filter(a -> a.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown area: " + value));
    }
}
