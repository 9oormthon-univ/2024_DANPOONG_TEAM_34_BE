package com.goormthom.danpoong.reboot.dto.response;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public record UserDetailDto(
        @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하여야 합니다.")
        String name,

        String nameEn,

        String email
) {
    @Builder
    public UserDetailDto(String name, String nameEn, String email) {
        this.name = name;
        this.nameEn = nameEn;
        this.email = email;
    }
}
