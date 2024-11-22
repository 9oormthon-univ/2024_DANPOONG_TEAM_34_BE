package com.goormthom.danpoong.reboot.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionListResponseDto(
        String mission,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean isDone
) {
}
