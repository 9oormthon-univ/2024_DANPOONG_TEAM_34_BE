package com.goormthom.danpoong.reboot.service.chat;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.dto.response.ReadChatAiResponseDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatUserResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.chat.ReadChatUseCase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadChatService implements ReadChatUseCase {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ReadChatResponseDto execute(UUID userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        ChatRoom chatRoom = chatRoomRepository.findByUserAndId(user, chatRoomId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATROOM));

        List<Chat> chats = chatRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        List<ReadChatUserResponseDto> readChatUserResponseDtos = chats.stream()
                .filter(chat -> chat.getSpeaker() == ESpeaker.USER)
                .map(this::chatUser)
                .collect(Collectors.toList());

        List<ReadChatAiResponseDto> readChatAIResponseDtos = chats.stream()
                .filter(chat -> chat.getSpeaker() != ESpeaker.USER)
                .map(this::chatAi)
                .collect(Collectors.toList());

        return ReadChatResponseDto.builder()
                .chatUserList(readChatUserResponseDtos)
                .chatAIList(readChatAIResponseDtos)
                .build();

    }

    private ReadChatUserResponseDto chatUser(Chat chat) {
        return ReadChatUserResponseDto.builder()
                .chatContent(chat.getChatContent())
                .imageUrl(chat.getImageUrl())
                .createAt(chat.getCreatedAt())
                .speaker(chat.getSpeaker())
                .build();
    }

    private ReadChatAiResponseDto chatAi(Chat chat) {
        return ReadChatAiResponseDto.builder()
                .responseContent(chat.getResponseContent())
                .createdAt(chat.getCreatedAt())
                .speaker(chat.getSpeaker())
                .build();
    }


}
