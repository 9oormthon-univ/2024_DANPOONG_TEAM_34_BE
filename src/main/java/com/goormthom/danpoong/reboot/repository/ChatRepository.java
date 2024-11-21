package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatRepository extends MongoRepository<Chat, String> {


    Optional<Chat> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatroomId);

    Integer countByChatRoomIdAndIsReadFalse(Long chatRoomId);

    List<Chat> findByChatRoomIdOrderByCreatedAtAsc(Long chatroomId);

}
