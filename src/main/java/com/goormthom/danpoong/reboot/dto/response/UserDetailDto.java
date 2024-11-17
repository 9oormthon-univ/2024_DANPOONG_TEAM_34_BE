package com.goormthom.danpoong.reboot.dto.response;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public record UserDetailDto(
        @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하여야 합니다.")
        String nickname
) {
    @Builder
    public UserDetailDto(String nickname) {
        this.nickname = nickname;
    }
}
