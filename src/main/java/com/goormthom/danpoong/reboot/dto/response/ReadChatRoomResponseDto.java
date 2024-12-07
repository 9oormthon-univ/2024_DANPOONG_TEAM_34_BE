package com.goormthom.danpoong.reboot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import java.time.LocalTime;
import lombok.Builder;


public record ReadChatRoomResponseDto(
        Long chatRoomId,
        EChatType eChatType,
        String title,
        String messagePreview,
        Integer nonReadCount,
        @JsonFormat(pattern = "HH:mm")
        LocalTime createdAt
) {
    @Builder
    public ReadChatRoomResponseDto(Long chatRoomId, EChatType eChatType, String title, String messagePreview, Integer nonReadCount, LocalTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.eChatType = eChatType;
        this.title = title;
        this.messagePreview = messagePreview;
        this.nonReadCount = nonReadCount;
        this.createdAt = createdAt.minusHours(9L);
    }

    public static ReadChatRoomResponseDto of(ChatRoom chatRoom, String messagePreview, Integer nonReadCount, LocalTime createdAt) {
        return ReadChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .eChatType(chatRoom.getChatType())
                .title(chatRoom.getTitle())
                .messagePreview(messagePreview)
                .nonReadCount(nonReadCount)
                .createdAt(createdAt)
                .build();
    }
}
