package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.Chat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatRepository extends MongoRepository<Chat, String> {


    Optional<Chat> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatroomId);

    Integer countByChatRoomIdAndIsReadFalse(Long chatRoomId);

    List<Chat> findByChatRoomIdOrderByCreatedAtAsc(Long chatroomId);

    List<Chat> findByChatRoomIdAndCreatedAtBetween(Long id, LocalDateTime startTime, LocalDateTime endTime);

    List<Chat> findByChatRoomIdAndSpeakerAndCreatedAtBetween(Long chatroomId, ESpeaker speaker, LocalDateTime startTime, LocalDateTime endTime);
}
