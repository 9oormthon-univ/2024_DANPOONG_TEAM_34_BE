package com.goormthom.danpoong.reboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "회원 추가 정보 | Req")
public record CreateOnBoardingRequestDto(
        @Schema(description = "닉네임", example = "단풍이")
        @NotNull(message = "이름을 입력해주세요")
        String name,

        @Schema(description = "닉네임", example = "단풍이")
        @NotNull(message = "이름을 입력해주세요")
        String nameEn,

        @Schema(description = "성별", example = "남자")
        @NotNull(message = "성별을 입력해야합니다.")
        String gender,

        @Schema(description = "생일", example = "2000-01-01")
        @NotNull(message = "생일을 입력해야합니다.")
        LocalDate birthday,

        @Schema(description = "지원동기", example = "일상적인 삶을 살기위해....")
        @NotNull(message = "지원동기를 입력해야합니다.")
        String motivation
) {
}
