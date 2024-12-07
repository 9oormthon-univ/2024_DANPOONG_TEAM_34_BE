package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.Meal;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import com.goormthom.danpoong.reboot.domain.type.EMissionStatus;
import com.goormthom.danpoong.reboot.dto.response.MissionListResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.MealRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.MissionListUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionListService implements MissionListUseCase {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public List<MissionListResponseDto> execute(UUID userId) {
        User user = getUser(userId);
        LocalDate today = LocalDate.now();
        LocalTime wakeUpTime = user.getAttendanceTime();

        List<EChatType> chatTypes = createMissionChatTypes(user, getUserMeals(user));

        return chatTypes.stream()
                .map(chatType -> buildMissionResponse(user, chatType, today, wakeUpTime))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(MissionListResponseDto::weight))
                .toList();
    }

    private MissionListResponseDto buildMissionResponse(User user, EChatType chatType, LocalDate today, LocalTime wakeUpTime) {
        LocalDateTime startTime = calculateStartTime(chatType, today, wakeUpTime);
        LocalDateTime endTime = calculateEndTime(chatType, today, wakeUpTime);

        EMissionStatus status = chatType == EChatType.OUTSIDE
                ? checkOutsideMissionStatus(user, today, startTime, endTime)
                : checkRegularMissionStatus(user, chatType, today, startTime, endTime);

        return MissionListResponseDto.builder()
                .weight(chatType.weight())
                .mission(chatType.chatTitle())
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .build();
    }

    private EMissionStatus checkOutsideMissionStatus(User user, LocalDate today, LocalDateTime startTime, LocalDateTime endTime) {
        boolean isSubTypeCompleted = Stream.of(EChatType.WALK, EChatType.MARKET, EChatType.PICTURE)
                .anyMatch(subType -> isMissionCompleted(filterChats(user, subType, today, startTime, endTime)));

        return isSubTypeCompleted ? EMissionStatus.SUCCESS : EMissionStatus.FAIL;
    }

    private EMissionStatus checkRegularMissionStatus(User user, EChatType chatType, LocalDate today, LocalDateTime startTime, LocalDateTime endTime) {
        boolean isCompleted = isMissionCompleted(filterChats(user, chatType, today, startTime, endTime));
        return isCompleted ? EMissionStatus.SUCCESS : EMissionStatus.FAIL;
    }

    private boolean isMissionCompleted(List<Chat> chats) {
        return chats.stream()
                .anyMatch(chat -> Boolean.TRUE.equals(chat.getIsCompleted()));
    }

    private List<Chat> filterChats(User user, EChatType chatType, LocalDate today, LocalDateTime startTime, LocalDateTime endTime) {
        return chatRoomRepository.findByUserAndChatType(user, chatType).stream()
                .flatMap(chatRoom -> chatRepository.findByChatRoomIdAndCreatedAtBetween(
                        chatRoom.getId(),
                        today.atStartOfDay().plusHours(9),
                        today.plusDays(1).atStartOfDay().plusHours(9)
                ).stream())
                .filter(chat -> chat.getIsCompleted() != null && isWithinTimeRange(chat.getCreatedAt().minusHours(9), startTime, endTime))
                .toList();
    }

    private boolean isWithinTimeRange(LocalDateTime createdAt, LocalDateTime startTime, LocalDateTime endTime) {
        return createdAt.isAfter(startTime) && createdAt.isBefore(endTime);
    }

    private List<EChatType> createMissionChatTypes(User user, List<Meal> meals) {
        List<EChatType> chatTypes = new ArrayList<>(List.of(EChatType.FOLD, EChatType.LEAVE));

        if (user.getIsOutside()) {
            chatTypes.add(EChatType.OUTSIDE);
        }

        meals.stream()
                .map(meal -> EMealTime.fromName(meal.getMealTime().getMeal()))
                .filter(this::isRelevantMealType)
                .map(this::mapMealToChatType)
                .forEach(chatTypes::add);

        return chatTypes;
    }

    private boolean isRelevantMealType(EMealTime mealTime) {
        return mealTime == EMealTime.MORNING || mealTime == EMealTime.LUNCH || mealTime == EMealTime.DINNER;
    }

    private EChatType mapMealToChatType(EMealTime mealTime) {
        return switch (mealTime) {
            case MORNING -> EChatType.MORNING;
            case LUNCH -> EChatType.LUNCH;
            case DINNER -> EChatType.DINNER;
        };
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }

    private List<Meal> getUserMeals(User user) {
        return mealRepository.findByUser(user);
    }

    private LocalDateTime calculateStartTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> date.atTime(wakeUpTime);
            case MORNING -> date.atTime(9, 0);
            case LUNCH -> date.atTime(12, 0);
            case DINNER -> date.atTime(19, 0);
            case OUTSIDE, WALK, MARKET, PICTURE -> date.atTime(15, 0);
            case LEAVE -> date.atTime(20, 0);
            case FREE -> null;
        };
    }

    private LocalDateTime calculateEndTime(EChatType chatType, LocalDate date, LocalTime wakeUpTime) {
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
}