package com.goormthom.danpoong.reboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "토큰 발급 | Req, Res")
public record JwtTokenDto(
        @Schema(description = "accessToken")
        @NotNull(message = "AccessToken은 null이 될 수 없습니다.")
        String accessToken,
        @Schema(description = "refreshToken")
        @NotNull(message = "RefreshToken은 null이 될 수 없습니다.")
        String refreshToken
) {
    @Builder
    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
