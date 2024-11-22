package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.dto.response.CalendarDetailResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.CalendarDetailUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarDetailService implements CalendarDetailUseCase {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public List<CalendarDetailResponseDto> execute(UUID userId, LocalDate date) {
        User user = findUserById(userId);
        List<ChatRoom> chatRooms = findChatRoomsByUser(user);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Chat> chats = findChatsForDateRange(chatRooms, startOfDay, endOfDay);

        return mapChatsToResponseDtos(chats, chatRooms);
    }

    private List<Chat> findChatsForDateRange(List<ChatRoom> chatRooms, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return chatRooms.stream()
                .map(ChatRoom::getId)
                .flatMap(chatRoomId -> findChatsByRoomId(chatRoomId, startOfDay, endOfDay).stream())
                .toList();
    }

    private List<Chat> findChatsByRoomId(Long chatRoomId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return chatRepository.findByChatRoomIdAndSpeakerAndCreatedAtBetween(
                chatRoomId, ESpeaker.USER, startOfDay.plusHours(9), endOfDay.plusHours(9)
        );
    }

    private List<ChatRoom> findChatRoomsByUser(User user) {
        return chatRoomRepository.findByUser(user);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }

    private List<CalendarDetailResponseDto> mapChatsToResponseDtos(List<Chat> chats, List<ChatRoom> chatRooms) {
        return chats.stream()
                .map(chat -> {
                    ChatRoom chatRoom = findChatRoomById(chat.getChatRoomId(), chatRooms);
                    return buildResponseDto(chat, chatRoom);
                })
                .toList();
    }

    private ChatRoom findChatRoomById(Long chatRoomId, List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .filter(chatRoom -> chatRoom.getId().equals(chatRoomId))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    private CalendarDetailResponseDto buildResponseDto(Chat chat, ChatRoom chatRoom) {
        return CalendarDetailResponseDto.builder()
                .createdAt(chat.getCreatedAt().minusHours(9))
                .chatType(chatRoom.getChatType().description())
                .imageUrl(chat.getImageUrl())
                .content(chat.getResponseContent())
                .build();
    }
}
