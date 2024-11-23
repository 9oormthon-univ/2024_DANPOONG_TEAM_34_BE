package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.type.EMissionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionListResponseDto(
        String mission,
        @JsonFormat(pattern = "HH:mm")
        LocalDateTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalDateTime endTime,
        EMissionStatus status,
        Long weight
) {
}

