package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.EChatType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type")
    private EChatType chatType;

    @Builder
    public ChatRoom(User user, String title, EChatType chatType) {
        this.user = user;
        this.title = title;
        this.chatType = chatType;
    }

    public static ChatRoom toEntity(User user, String title, EChatType chatType) {
        return ChatRoom.builder()
                .user(user)
                .title(title)
                .chatType(chatType)
                .build();
    }
}
