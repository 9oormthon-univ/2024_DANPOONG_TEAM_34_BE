package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import java.time.LocalDateTime;
import lombok.Builder;

public record ReadChatResponseDto(
        String chatContent,

        String imageUrl,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm::ss")
        LocalDateTime createAt,

        ESpeaker speaker
) {
    @Builder
    public ReadChatResponseDto(String chatContent, String imageUrl, LocalDateTime createAt, ESpeaker speaker) {
        this.chatContent = chatContent;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
        this.speaker = speaker;
    }

}
