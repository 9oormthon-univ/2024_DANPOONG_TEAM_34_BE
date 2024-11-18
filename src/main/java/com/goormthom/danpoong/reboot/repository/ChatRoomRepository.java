package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
