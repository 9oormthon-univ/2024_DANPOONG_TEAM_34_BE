package com.goormthom.danpoong.reboot.dto.common;

import com.goormthom.danpoong.reboot.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public sealed class ExceptionDto permits ArgumentNotValidExceptionDto {
    @NotNull
    private final Integer code;

    @NotNull private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}
