package com.goormthom.danpoong.reboot.dto.response;

import lombok.Builder;

@Builder
public record RemainPeriodResponseDto(
        Long contractPeriod,
        Long progressPeriod,
        Long remainPeriod
) {
}
