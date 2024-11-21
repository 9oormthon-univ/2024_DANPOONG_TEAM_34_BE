package com.goormthom.danpoong.reboot.service.chat;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.dto.response.ReadChatRoomResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.chat.ReadChatRoomUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadChatRoomService implements ReadChatRoomUseCase {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Override
    public List<ReadChatRoomResponseDto> execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        List<ChatRoom> chatRooms = chatRoomRepository.findByUser(user);

        return chatRooms.stream()
                .map(this::chatRoomInfo)
                .toList();
    }

    private ReadChatRoomResponseDto chatRoomInfo(ChatRoom chatRoom) {

        return ReadChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .eChatType(chatRoom.getChatType())
                .title(chatRoom.getTitle())
                .messagePreview(checkMessage(chatRoom))
                .nonReadCount(checkIsRead(chatRoom))
                .build();
    }

    private String checkMessage(ChatRoom chatRoom) {
        Optional<Chat> chatRecent = chatRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
        return chatRecent.flatMap(chat ->
                Optional.ofNullable(chat.getResponseContent())
        ).orElse("아직 메세지가 없습니다.");
    }

    private Integer checkIsRead(ChatRoom chatRoom) {
        return chatRepository.countByChatRoomIdAndIsReadFalse(chatRoom.getId());
    }

}
