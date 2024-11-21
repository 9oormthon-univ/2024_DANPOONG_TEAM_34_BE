package com.goormthom.danpoong.reboot.service.chat;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.request.CreateChatRequestDto;
import com.goormthom.danpoong.reboot.dto.response.PromaDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.chat.CreateChatUseCase;
import com.goormthom.danpoong.reboot.util.PromaUtil;
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
    public PromaDto execute(CreateChatRequestDto createChatRequestDto, String imageUrl, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        PromaDto promaDto = promaUtil.generateAnswer(createChatRequestDto.question(), imageUrl, user.getEmail(), createChatRequestDto.eChatType());

        ChatRoom chatRoom = chatRoomRepository.findByUserAndChatType(user, createChatRequestDto.eChatType())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATROOM));

        Chat question = Chat.toEntityQuestion(chatRoom.getId(), createChatRequestDto.question(), imageUrl);
        chatRepository.save(question);
        Chat answer = Chat.toEntityAnswer(chatRoom.getId(), promaDto.messageAnswer());
        chatRepository.save(answer);

        return promaDto;
    }
}
