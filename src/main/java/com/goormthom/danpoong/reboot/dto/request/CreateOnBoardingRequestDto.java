package com.goormthom.danpoong.reboot.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateOnBoardingRequestDto(
        @NotNull(message = "이름을 입력해주세요")
        String nickname,

        @NotNull(message = "성별을 입력해야합니다.")
        String gender,

        @NotNull(message = "생일을 입력해야합니다.")
        LocalDate birthday,

        @NotNull(message = "지원동기를 입력해야합니다.")
        String motivation
) {
}
