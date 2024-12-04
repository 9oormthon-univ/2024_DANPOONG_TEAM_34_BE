package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.EMissionStatus;
import com.goormthom.danpoong.reboot.dto.response.CalendarItemResponseDto;
import com.goormthom.danpoong.reboot.dto.response.CalendarResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.CalendarUseCase;
import java.util.Map.Entry;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService implements CalendarUseCase {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CalendarResponseDto execute(UUID userId, Long groupType) {
        User user = findUserById(userId);
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUser(user);
        Map<LocalDate, Map<EChatType, Map<EMissionStatus, Integer>>> missionResults = calculateMissionResults(user, chatRoomList);

        Long[] counts = {0L, 0L, 0L};
        List<CalendarItemResponseDto> calendarItems = createCalendarItems(missionResults, groupType, counts);

        return CalendarResponseDto.builder()
                .success(counts[0])
                .unclear(counts[1])
                .fail(counts[2])
                .workStartTime(user.getWorkStartTime())
                .workEndTime(user.getWorkEndTime())
                .currentTime(LocalDate.now())
                .calendarItemResponseDto(calendarItems)
                .build();
    }

    private Map<LocalDate, Map<EChatType, Map<EMissionStatus, Integer>>> calculateMissionResults(User user, List<ChatRoom> chatRoomList) {
        LocalDate startDate = user.getWorkStartTime();
        LocalDate endDate = LocalDate.now();

        return startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toMap(
                        date -> date,
                        date -> calculateMissionCountsForDate(date, chatRoomList, user)
                ));
    }

    private Map<EChatType, Map<EMissionStatus, Integer>> calculateMissionCountsForDate(LocalDate date, List<ChatRoom> chatRoomList, User user) {
        return chatRoomList.stream()
                .collect(Collectors.toMap(
                        ChatRoom::getChatType,
                        chatRoom -> calculateMissionStatusCounts(chatRoom, date, user)
                ));
    }

    private Map<EMissionStatus, Integer> calculateMissionStatusCounts(ChatRoom chatRoom, LocalDate date, User user) {
        LocalDateTime startTime = getStartTime(chatRoom.getChatType(), date, user.getAttendanceTime());
        LocalDateTime endTime = getEndTime(chatRoom.getChatType(), date, user.getAttendanceTime());

        EMissionStatus status = evaluateMissionStatus(chatRoom, startTime, endTime);

        return Collections.singletonMap(status, 1);
    }

    private List<CalendarItemResponseDto> createCalendarItems(
            Map<LocalDate, Map<EChatType, Map<EMissionStatus, Integer>>> missionResults,
            Long groupType, Long[] counts) {

        return missionResults.entrySet().stream()
                .map(entry -> createCalendarItem(entry, groupType, counts))
                .collect(Collectors.toList());
    }

    private CalendarItemResponseDto createCalendarItem(Entry<LocalDate, Map<EChatType, Map<EMissionStatus, Integer>>> entry, Long groupType, Long[] counts) {
        LocalDate date = entry.getKey();
        Map<EChatType, Map<EMissionStatus, Integer>> missionCounts = entry.getValue();
        String status = evaluateDayStatus(missionCounts, groupType);

        updateCounts(status, counts);

        return CalendarItemResponseDto.builder()
                .itemTime(date.atStartOfDay())
                .status(status)
                .build();
    }

    private void updateCounts(String status, Long[] counts) {
        switch (status) {
            case "완료" -> counts[0]++;
            case "노력필요" -> counts[1]++;
            case "업무불참" -> counts[2]++;
        }
    }

    private String evaluateDayStatus(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts, Long groupType) {
        return switch (groupType.intValue()) {
            case 0 -> evaluateWorkStatus(missionCounts);
            case 1 -> evaluateMealStatus(missionCounts);
            case 2 -> evaluateFieldStatus(missionCounts);
            default -> EMissionStatus.FAIL.getDescription();
        };
    }

    private String evaluateWorkStatus(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts) {
        boolean morningSuccess = hasSuccessfulMission(missionCounts, EChatType.FOLD);
        boolean leaveSuccess = hasSuccessfulMission(missionCounts, EChatType.LEAVE);

        if (morningSuccess && leaveSuccess) return EMissionStatus.SUCCESS.getDescription();
        if (morningSuccess || leaveSuccess) return EMissionStatus.UNCLEAR.getDescription();
        return EMissionStatus.FAIL.getDescription();
    }

    private String evaluateMealStatus(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts) {
        long mealSuccessCount = countSuccessfulMeals(missionCounts);
        if (mealSuccessCount == 3) return EMissionStatus.SUCCESS.getDescription();
        if (mealSuccessCount >= 1) return EMissionStatus.UNCLEAR.getDescription();
        return EMissionStatus.FAIL.getDescription();
    }

    private String evaluateFieldStatus(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts) {
        boolean fieldSuccess = hasSuccessfulMission(missionCounts, EChatType.WALK) ||
                hasSuccessfulMission(missionCounts, EChatType.MARKET) ||
                hasSuccessfulMission(missionCounts, EChatType.PICTURE);
        return fieldSuccess ? EMissionStatus.SUCCESS.getDescription() : EMissionStatus.FAIL.getDescription();
    }

    private boolean hasSuccessfulMission(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts, EChatType chatType) {
        return missionCounts.containsKey(chatType) && missionCounts.get(chatType).getOrDefault(EMissionStatus.SUCCESS, 0) > 0;
    }

    private long countSuccessfulMeals(Map<EChatType, Map<EMissionStatus, Integer>> missionCounts) {
        return Stream.of(EChatType.MORNING, EChatType.LUNCH, EChatType.DINNER)
                .filter(chatType -> hasSuccessfulMission(missionCounts, chatType))
                .count();
    }

    private LocalDateTime getStartTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> date.atTime(wakeUpTime);
            case MORNING -> date.atTime(9, 0);
            case LUNCH -> date.atTime(12, 0);
            case DINNER -> date.atTime(17, 0);
            case OUTSIDE, WALK, MARKET, PICTURE -> date.atTime(15, 0);
            case LEAVE -> date.atTime(20, 0);
            case FREE -> null;
        };
    }

    private LocalDateTime getEndTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> date.atTime(wakeUpTime.plusMinutes(60));
            case MORNING -> date.atTime(10, 0);
            case LUNCH -> date.atTime(13, 0);
            case DINNER -> date.atTime(20, 0);
            case OUTSIDE, WALK, MARKET, PICTURE -> date.atTime(16, 0);
            case LEAVE -> date.atTime(21, 0);
            case FREE -> null;
        };
    }

    private EMissionStatus evaluateMissionStatus(ChatRoom chatRoom, LocalDateTime startTime, LocalDateTime endTime) {
        return chatRepository.findByChatRoomIdAndCreatedAtBetween(chatRoom.getId(), startTime.plusHours(9), endTime.plusHours(9)).stream()
                .filter(chat -> chat.getIsCompleted() != null)
                .anyMatch(Chat::getIsCompleted) ? EMissionStatus.SUCCESS : EMissionStatus.FAIL;
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}






