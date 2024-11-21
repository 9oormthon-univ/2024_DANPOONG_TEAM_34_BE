package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record LastChatInfo(
        String messagePreview,

        @JsonFormat(pattern = "HH:MM")
        LocalTime createdAt
) {
}
