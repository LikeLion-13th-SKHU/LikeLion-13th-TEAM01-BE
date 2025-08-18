package com.saym.eventory.store.api.dto.request;

public record MenuRequestDto(
        String menuName,
        int price,
        boolean isSignature
) {
}