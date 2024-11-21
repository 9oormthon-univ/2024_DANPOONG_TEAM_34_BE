package com.goormthom.danpoong.reboot.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PromaDto(
        @NotNull(message = "AI 답변을 받지 못했습니다. 다시 요청해주세요")
        String messageAnswer
) {
    public PromaDto(String messageAnswer) {
        this.messageAnswer = messageAnswer;
    }
    public static PromaDto of(String messageAnswer) {
        return PromaDto.builder()
                .messageAnswer(messageAnswer)
                .build();
    }
}
