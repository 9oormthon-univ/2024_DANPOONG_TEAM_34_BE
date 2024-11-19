package com.goormthom.danpoong.reboot.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public record JwtTokenDto(
        @NotNull(message = "AccessToken은 null이 될 수 없습니다.")
        String accessToken,
        @NotNull(message = "RefreshToken은 null이 될 수 없습니다.")
        String refreshToken
) {
    @Builder
    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
