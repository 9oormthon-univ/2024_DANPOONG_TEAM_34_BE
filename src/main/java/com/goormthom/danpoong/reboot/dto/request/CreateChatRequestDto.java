package com.goormthom.danpoong.reboot.dto.request;

import com.goormthom.danpoong.reboot.domain.type.EChatType;
import jakarta.validation.constraints.NotNull;

public record CreateChatRequestDto(
        @NotNull(message = "보고를 입력해주세요")
        String question,

        @NotNull(message = "채팅의 타입을 입력해주세요")
        EChatType eChatType
) {
}
