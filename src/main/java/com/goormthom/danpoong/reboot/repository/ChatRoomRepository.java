package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUser(User user);
}
