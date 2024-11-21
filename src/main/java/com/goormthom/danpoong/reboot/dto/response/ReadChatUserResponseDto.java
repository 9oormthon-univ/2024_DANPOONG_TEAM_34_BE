package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import java.time.LocalDateTime;
import lombok.Builder;

public record ReadChatUserResponseDto(
        String chatContent,

        String imageUrl,

        @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
        LocalDateTime createAt,

        ESpeaker speaker
) {
    @Builder
    public ReadChatUserResponseDto(String chatContent, String imageUrl, LocalDateTime createAt, ESpeaker speaker) {
        this.chatContent = chatContent;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
        this.speaker = speaker;
    }

}
