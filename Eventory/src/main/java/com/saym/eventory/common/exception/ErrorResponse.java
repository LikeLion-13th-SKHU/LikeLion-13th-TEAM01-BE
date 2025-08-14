package com.saym.eventory.common.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {}