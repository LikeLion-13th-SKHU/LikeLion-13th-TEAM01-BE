package com.saym.eventory.store.api.dto.response;

import com.saym.eventory.store.domain.Store;
import java.util.List;
import java.util.stream.Collectors;

public record StoreResponseDto(
        Long id,
        String name,
        String phoneNumber,
        String pictureUrl,
        String openTime,
        String closeTime,
        String operatingHours,
        String address,
        String parkingNote,
        List<MenuResponseDto> menus  // Response용 DTO로 변경
) {
    public static StoreResponseDto from(Store store) {
        String openTimeStr = store.getOpenTime().toString();
        String closeTimeStr = store.getCloseTime().toString();
        String operatingHours = openTimeStr + "~" + closeTimeStr;

        List<MenuResponseDto> menuDtos = store.getMenus().stream()
                .map(menu -> new MenuResponseDto(
                        menu.getId(),
                        menu.getMenuName(),
                        menu.getPrice(),
                        menu.isSignature()
                ))
                .collect(Collectors.toList());

        return new StoreResponseDto(
                store.getId(),
                store.getName(),
                store.getPhoneNumber(),
                store.getPictureUrl(),
                openTimeStr,
                closeTimeStr,
                operatingHours,
                store.getAddress() + " " + store.getAddressDetail(),
                store.getParkingNote(),
                menuDtos
        );
    }
}
