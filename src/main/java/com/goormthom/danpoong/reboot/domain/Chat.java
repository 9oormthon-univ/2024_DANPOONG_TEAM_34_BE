package com.goormthom.danpoong.reboot.domain;

import jakarta.persistence.Id;
import lombok.AccessLevel;
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
    private LocalDate createdAt;
}
