package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Document(collection = "chats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    private String id;

    @Field(name = "chat_room_id")
    private Long chatRoomId;

    @Field(name = "chat_content")
    private String chatContent;

    @Field(name = "image_url")
    private String imageUrl;

    @Field(name = "response_content")
    private String responseContent;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "speaker")
    private ESpeaker speaker;

    @Field(name = "is_read")
    private Boolean isRead;

    @Builder
    public Chat(Long chatRoomId, String chatContent, String imageUrl, String responseContent, ESpeaker speaker, Boolean isRead) {
        this.chatRoomId = chatRoomId;
        this.chatContent = chatContent;
        this.imageUrl = imageUrl;
        this.responseContent = responseContent;
        this.createdAt = LocalDateTime.now().plusHours(9);
        this.speaker = speaker;
        this.isRead = isRead;
    }
    public static Chat toEntityQuestion(Long chatRoomId, String chatContent, String imageUrl) {
        return Chat.builder()
                .chatRoomId(chatRoomId)
                .chatContent(chatContent)
                .imageUrl(imageUrl)
                .speaker(ESpeaker.USER)
                .isRead(true)
                .build();
    }

    public static Chat toEntityAnswer(Long chatRoomId, String responseContent) {
        return Chat.builder()
                .chatRoomId(chatRoomId)
                .responseContent(responseContent)
                .speaker(ESpeaker.AI)
                .isRead(true)
                .build();
    }
}
