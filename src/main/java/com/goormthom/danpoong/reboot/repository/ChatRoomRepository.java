package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {
    List<ChatRoom> findByUser(User user);
    Optional<ChatRoom> findByUserAndId(User user, Long id);
    List<ChatRoom> findAllByUserInAndChatType(List<User> list, EChatType chatType);
}
