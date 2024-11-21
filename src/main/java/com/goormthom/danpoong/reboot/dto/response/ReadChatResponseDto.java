package com.goormthom.danpoong.reboot.dto.response;

import java.util.List;
import lombok.Builder;

public record ReadChatResponseDto(
        List<ReadChatUserResponseDto> chatUserList,

        List<ReadChatAiResponseDto> chatAIList
) {
    @Builder
    public ReadChatResponseDto(List<ReadChatUserResponseDto> chatUserList, List<ReadChatAiResponseDto> chatAIList) {
        this.chatUserList = chatUserList;
        this.chatAIList = chatAIList;
    }

}
