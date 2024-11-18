package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
