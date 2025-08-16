package com.saym.eventory.menu.api.dto;

import com.saym.eventory.menu.domain.Menu;

public record MenuResponseDto(
        Long menuId,
        String menuName,
        Long price
) {
    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuPrice()
        );
    }
}