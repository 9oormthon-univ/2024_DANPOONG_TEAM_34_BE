package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.EMissionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionListResponseDto(
        EChatType mission,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalDateTime startTime,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalDateTime endTime,
        EMissionStatus status
) {
}

