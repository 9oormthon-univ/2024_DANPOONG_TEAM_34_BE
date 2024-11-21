package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;

import java.util.Optional;

public interface ChatRoomCustomRepository {
    Optional<ChatRoom> findByUserAndChatType(User user, EChatType chatType);
}
