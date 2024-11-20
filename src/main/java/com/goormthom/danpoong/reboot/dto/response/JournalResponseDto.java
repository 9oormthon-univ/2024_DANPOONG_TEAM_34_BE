package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record JournalResponseDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate workStartTime,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate workEndTime,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate currentTime
) {
}
