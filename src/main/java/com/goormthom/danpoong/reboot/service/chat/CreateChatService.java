package com.goormthom.danpoong.reboot.service.chat;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.dto.request.CreateChatRequestDto;
import com.goormthom.danpoong.reboot.dto.response.PromaDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.chat.CreateChatUseCase;
import com.goormthom.danpoong.reboot.util.PromaUtil;
import com.goormthom.danpoong.reboot.util.S3Util;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateChatService implements CreateChatUseCase {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PromaUtil promaUtil;

    @Override
    public ReadChatResponseDto execute(String question, EChatType eChatType, String imageUrl, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        PromaDto promaDto = promaUtil.generateAnswer(question, imageUrl, user.getEmail(), eChatType);

        ChatRoom chatRoom = chatRoomRepository.findByUserAndChatType(user, eChatType)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATROOM));

        Chat questionChat = Chat.toEntity(chatRoom.getId(), question, imageUrl, ESpeaker.USER, promaDto.isCompleted());
        chatRepository.save(questionChat);
        Chat answerChat = Chat.toEntity(chatRoom.getId(), promaDto.messageAnswer(), null, ESpeaker.AI, null);
        chatRepository.save(answerChat);

        return ReadChatResponseDto.builder()
                .chatContent(promaDto.messageAnswer())
                .createAt(answerChat.getCreatedAt())
                .speaker(ESpeaker.AI)
                .isCompleted(promaDto.isCompleted())
                .build();
    }
}
