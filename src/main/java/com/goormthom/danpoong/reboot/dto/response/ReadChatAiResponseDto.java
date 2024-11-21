package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import java.time.LocalDateTime;
import lombok.Builder;

public record ReadChatAiResponseDto(
        String responseContent,
        @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
        LocalDateTime createdAt,
        ESpeaker speaker
) {
    @Builder
    public ReadChatAiResponseDto(String responseContent, LocalDateTime createdAt, ESpeaker speaker) {
        this.responseContent = responseContent;
        this.createdAt = createdAt;
        this.speaker = speaker;
    }

}
