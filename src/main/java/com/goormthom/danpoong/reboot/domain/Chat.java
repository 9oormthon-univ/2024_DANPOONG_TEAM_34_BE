package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Document(collection = "chats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Chat {
    @Id
    private String id;

    @Field(name = "chat_room_id")
    private Long chatRoomId;

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

    @Field(name = "is_completed")
    private Boolean isCompleted;

    @Builder
    public Chat(Long chatRoomId, String responseContent, ESpeaker speaker, Boolean isRead) {
        this.chatRoomId = chatRoomId;
        this.responseContent = responseContent;
        this.createdAt = LocalDateTime.now().plusHours(9);
        this.speaker = speaker;
        this.isRead = isRead;
        this.isCompleted = false;
    }
    public static Chat toEntity(Long chatRoomId, String responseContent, ESpeaker speaker) {
        return Chat.builder()
                .chatRoomId(chatRoomId)
                .responseContent(responseContent)
                .speaker(speaker)
                .isRead(true)
                .build();
    }

    public void updateIsRead() {
        this.isRead = true;
    }

    public void updateImageUrl(String imageUrl, Boolean isCompleted) {
        this.imageUrl = imageUrl;
        this.isCompleted = isCompleted;
    }

}
