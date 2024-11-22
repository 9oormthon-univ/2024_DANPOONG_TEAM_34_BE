package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

// 해당 날짜의 미션 수행 여부
@Builder
public record CalendarItemResponseDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime itemTime,
        String status
) {
}
