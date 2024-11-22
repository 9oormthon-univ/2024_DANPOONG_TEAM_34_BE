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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionListService implements MissionListUseCase {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<MissionListResponseDto> execute(UUID userId) {
        User user = findUserById(userId);

        // 사용자의 모든 ChatRoom 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findByUser(user);

        // 사용자의 기상시간 가져오기
        LocalTime wakeUpTime = user.getAttendanceTime(); // User 엔티티에서 기상시간 반환

        return chatRooms.stream()
                .map(chatRoom -> evaluateMission(chatRoom, LocalDate.now(), wakeUpTime))
                .sorted(Comparator.comparing(dto -> dto.mission().weight())) // weight 기준 오름차순 정렬
                .toList();
    }

    private MissionListResponseDto evaluateMission(ChatRoom chatRoom, LocalDate attendanceDate, LocalTime wakeUpTime) {
        EChatType chatType = chatRoom.getChatType();

        // 미션의 인증 가능 시작/마감 시간 계산
        LocalDateTime startTime = calculateStartTime(chatType, attendanceDate, wakeUpTime);
        LocalDateTime endTime = calculateEndTime(chatType, attendanceDate, wakeUpTime);

        // 미션 관련 Chat 조회
        List<Chat> chats = chatRepository.findByChatRoomIdAndCreatedAtBetween(
                chatRoom.getId(), startTime, endTime
        );

        EMissionStatus status = determineMissionStatus(chats);

        return MissionListResponseDto.builder()
                .mission(chatType)
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .build();
    }

    private LocalDateTime calculateStartTime(EChatType chatType, LocalDate attendanceDate, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> attendanceDate.atTime(wakeUpTime); // 기상시간
            case MORNING -> attendanceDate.atTime(9, 0); // 아침 식사 고정 시간
            case LUNCH -> attendanceDate.atTime(12, 0); // 점심 식사 고정 시간
            case DINNER -> attendanceDate.atTime(17, 0); // 저녁 식사 고정 시간
            case WALK, MARKET, PICTURE -> attendanceDate.atTime(15, 0); // 걷기, 장보기, 출사 고정 시간
            case LEAVE -> attendanceDate.atTime(20, 0); // 퇴근 고정 시간
        };
    }

    private LocalDateTime calculateEndTime(EChatType chatType, LocalDate attendanceDate, LocalTime wakeUpTime) {
        return switch (chatType) {
            case FOLD -> attendanceDate.atTime(wakeUpTime.plusMinutes(30)); // 기상시간 + 30분
            case MORNING -> attendanceDate.atTime(10, 0); // 아침 마감
            case LUNCH -> attendanceDate.atTime(13, 0); // 점심 마감
            case DINNER -> attendanceDate.atTime(20, 0); // 저녁 마감
            case WALK, MARKET, PICTURE -> attendanceDate.atTime(16, 0); // 걷기, 장보기, 출사 마감
            case LEAVE -> attendanceDate.atTime(21, 0); // 퇴근 마감
        };
    }

    private EMissionStatus determineMissionStatus(List<Chat> chats) {
        if (chats.isEmpty()) {
            return EMissionStatus.FAIL; // X
        }

        boolean isCompleted = chats.stream().anyMatch(Chat::getIsCompleted); // 사진 판별 성공 여부
        if (isCompleted) {
            return EMissionStatus.SUCCESS; // O
        }

        // 인증 가능 시간 내 메시지가 있지만 사진 판별 실패한 경우
        return EMissionStatus.UNCLEAR; // 세모
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}


