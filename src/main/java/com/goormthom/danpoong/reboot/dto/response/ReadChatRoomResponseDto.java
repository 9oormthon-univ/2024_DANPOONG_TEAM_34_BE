package com.goormthom.danpoong.reboot.dto.response;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import lombok.Builder;


public record ReadChatRoomResponseDto(
        Long chatRoomId,
        EChatType eChatType,
        String title,
        String messagePreview,
        Integer nonReadCount
) {
    @Builder
    public ReadChatRoomResponseDto(Long chatRoomId, EChatType eChatType, String title, String messagePreview, Integer nonReadCount) {
        this.chatRoomId = chatRoomId;
        this.eChatType = eChatType;
        this.title = title;
        this.messagePreview = messagePreview;
        this.nonReadCount = nonReadCount;
    }

    public static ReadChatRoomResponseDto of(ChatRoom chatRoom, String messagePreview, Integer nonReadCount) {
        return ReadChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .eChatType(chatRoom.getChatType())
                .title(chatRoom.getTitle())
                .messagePreview(messagePreview)
                .nonReadCount(nonReadCount)
                .build();
    }
}
