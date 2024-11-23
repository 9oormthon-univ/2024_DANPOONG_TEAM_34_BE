package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CalendarDetailResponseDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalDateTime createdAt,
        String chatType,
        String imageUrl,
        String content

) {
}
