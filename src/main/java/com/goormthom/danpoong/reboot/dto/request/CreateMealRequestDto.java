package com.goormthom.danpoong.reboot.dto.request;

import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import jakarta.validation.constraints.NotNull;

public record CreateMealRequestDto(
        @NotNull(message = "식사 입력을 해주세요")
        EMealTime mealTime
) {
}
