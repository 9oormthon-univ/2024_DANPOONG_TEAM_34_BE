package com.goormthom.danpoong.reboot.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PromaMissionDto(
        @NotNull(message = "AI 답변을 받지 못했습니다. 다시 요청해주세요")
        String messageAnswer,

        @NotNull(message = "AI 미션검사를 받지 못했습니다. 다시 요청해주세요")
        Boolean isCompleted
) {
    public PromaMissionDto(String messageAnswer, Boolean isCompleted) {
        this.messageAnswer = messageAnswer;
        this.isCompleted = isCompleted;
    }
    public static PromaMissionDto of(String messageAnswer, Boolean isCompleted) {
        return PromaMissionDto.builder()
                .messageAnswer(messageAnswer)
                .isCompleted(isCompleted)
                .build();
    }
}
