package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.EMissionStatus;
import com.goormthom.danpoong.reboot.dto.response.MissionListResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.MissionListUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionListService implements MissionListUseCase {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<MissionListResponseDto> execute(UUID userId) {
        User user = findUserById(userId);
        LocalTime wakeUpTime = user.getAttendanceTime();
        LocalDate today = LocalDate.now();

        return getMissionListForUser(user, today, wakeUpTime);
    }

    private List<MissionListResponseDto> getMissionListForUser(User user, LocalDate today, LocalTime wakeUpTime) {
        return chatRoomRepository.findByUser(user).stream()
                .map(chatRoom -> processMissionForChatRoom(chatRoom, today, wakeUpTime))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(MissionListResponseDto::weight))
                .toList();
    }

    private MissionListResponseDto processMissionForChatRoom(ChatRoom chatRoom, LocalDate today, LocalTime wakeUpTime) {
        LocalDateTime startTime = getStartTime(chatRoom.getChatType(), today, wakeUpTime);
        LocalDateTime endTime = getEndTime(chatRoom.getChatType(), today, wakeUpTime);

        List<Chat> todaysChats = filterTodaysChats(chatRoom, today);

        if (todaysChats.isEmpty()) {
            return null;
        }

        boolean isMissionCompleted = checkMissionCompletion(todaysChats, startTime, endTime);

        EMissionStatus status = determineMissionStatus(isMissionCompleted);
        return buildMissionListResponseDto(chatRoom.getChatType(), startTime, endTime, status);
    }

    private List<Chat> filterTodaysChats(ChatRoom chatRoom, LocalDate today) {
        return chatRepository.findByChatRoomIdAndCreatedAtBetween(
                chatRoom.getId(),
                today.atStartOfDay().plusHours(9),
                today.plusDays(1).atStartOfDay().plusHours(9)
        );
    }

    private boolean checkMissionCompletion(List<Chat> chats, LocalDateTime startTime, LocalDateTime endTime) {
        if (chats == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Input parameters cannot be null");
        }

        return chats.stream()
                .filter(chat -> chat != null && chat.getCreatedAt() != null)
                .filter(chat -> chat.getCreatedAt().isAfter(startTime) && chat.getCreatedAt().isBefore(endTime))
                .anyMatch(chat -> Boolean.TRUE.equals(chat.getIsCompleted()));
    }


    private EMissionStatus determineMissionStatus(boolean isMissionCompleted) {
        return isMissionCompleted ? EMissionStatus.SUCCESS : EMissionStatus.FAIL;
    }

    private LocalDateTime getStartTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> date.atTime(wakeUpTime);
            case MORNING -> date.atTime(9, 0);
            case LUNCH -> date.atTime(12, 0);
            case DINNER -> date.atTime(17, 0);
            case WALK, MARKET, PICTURE -> date.atTime(15, 0);
            case LEAVE -> date.atTime(20, 0);
        };
    }

    private LocalDateTime getEndTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> date.atTime(wakeUpTime.plusMinutes(60));
            case MORNING -> date.atTime(10, 0);
            case LUNCH -> date.atTime(13, 0);
            case DINNER -> date.atTime(20, 0);
            case WALK, MARKET, PICTURE -> date.atTime(16, 0);
            case LEAVE -> date.atTime(21, 0);
        };
    }

    private MissionListResponseDto buildMissionListResponseDto(EChatType chatType, LocalDateTime startTime, LocalDateTime endTime, EMissionStatus status) {
        return MissionListResponseDto.builder()
                .weight(chatType.weight())
                .mission(chatType.getDescription())
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}