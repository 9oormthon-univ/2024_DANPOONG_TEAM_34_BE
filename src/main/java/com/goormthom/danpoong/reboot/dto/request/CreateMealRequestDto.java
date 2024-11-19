package com.goormthom.danpoong.reboot.dto.request;

import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateMealRequestDto(
        @Schema(description = "식사 시간", example = "MORNING | LUNCH | DINNER")
        @NotNull(message = "식사 입력을 해주세요")
        EMealTime mealTime
) {
}
