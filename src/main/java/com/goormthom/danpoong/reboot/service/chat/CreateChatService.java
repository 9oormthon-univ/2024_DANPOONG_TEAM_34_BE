package com.goormthom.danpoong.reboot.service.chat;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.dto.response.PromaMissionDto;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateChatService implements CreateChatUseCase {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PromaUtil promaUtil;
    private final S3Util s3Util;

    @Override
    public ReadChatResponseDto execute(String question, EChatType eChatType, MultipartFile file, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        ChatRoom chatRoom = chatRoomRepository.findByUserAndChatType(user, eChatType)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATROOM));

        Chat chat = Chat.toEntity(chatRoom.getId(),question,ESpeaker.USER);
        String answer = null;
        Boolean isCompleted = false;
        switch (eChatType) {
            case FREE -> {
                answer = promaUtil.generatorFreeChatAnswer(question, user.getEmail(), eChatType);
            }
            default -> {
                String imageUrl = s3Util.upload(file);
                PromaMissionDto promaMissionDto = promaUtil.generateAnswer(question, imageUrl, user.getEmail(), eChatType);
                chat.updateImageUrl(promaMissionDto.messageAnswer(), promaMissionDto.isCompleted());
                answer = promaMissionDto.messageAnswer();
            }
        }

        Chat answerChat = Chat.toEntity(chatRoom.getId(), answer, ESpeaker.AI);
        chatRepository.save(chat);
        chatRepository.save(answerChat);

        return ReadChatResponseDto.builder()
                .chatContent(answer)
                .createAt(answerChat.getCreatedAt())
                .speaker(ESpeaker.AI)
                .isCompleted(isCompleted)
                .build();
    }
}
