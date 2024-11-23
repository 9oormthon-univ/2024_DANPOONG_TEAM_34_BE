package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
@Builder
public record CalendarResponseDto(
        Long success,
        Long unclear,
        Long fail,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate workStartTime,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate workEndTime,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate currentTime,
        List<CalendarItemResponseDto> calendarItemResponseDto
) {
}

