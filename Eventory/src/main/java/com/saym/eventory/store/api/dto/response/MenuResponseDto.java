package com.saym.eventory.store.api.dto.response;

public record MenuResponseDto(
        Long id,
        String menuName,
        int price,
        boolean isSignature
) {
}
