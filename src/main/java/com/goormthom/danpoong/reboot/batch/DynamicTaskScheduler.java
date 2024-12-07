package com.goormthom.danpoong.reboot.batch;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicTaskScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleSingleUserTask(User user) {
        Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms = preloadChatRooms(List.of(user));
        scheduleTask(user, preloadedChatRooms);
    }

    @Transactional
    public void scheduleAttendanceTasks() {
        List<User> activeUsers = getActiveUsers();
        Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms = preloadChatRooms(activeUsers);

        activeUsers.forEach(user -> scheduleTask(user, preloadedChatRooms));
        logScheduledTasks();
    }

    private List<User> getActiveUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getAttendanceTime() != null && user.getWorkEndTime().isAfter(LocalDate.now()))
                .toList();
    }

    private Map<UUID, Map<EChatType, ChatRoom>> preloadChatRooms(List<User> users) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserInAndChatType(users, EChatType.FOLD);
        return chatRooms.stream()
                .collect(Collectors.groupingBy(
                        chatRoom -> chatRoom.getUser().getId(),
                        Collectors.toMap(
                                ChatRoom::getChatType,
                                Function.identity()
                        )
                ));
    }

    private void scheduleTask(User user, Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms) {
        long delay = calculateDelay(user.getAttendanceTime());
        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> sendAttendanceMessages(user, preloadedChatRooms),
                new Date(System.currentTimeMillis() + delay)
        );

        ScheduledFuture<?> existingTask = scheduledTasks.put(user.getId(), future);
        if (existingTask != null) {
            existingTask.cancel(false);
            log.info("기존 작업 취소: 사용자 {}", user.getNickname());
        }

        log.info("스케줄링 예약: 사용자 {}, 지연: {} ms", user.getNickname(), delay);
    }

    private long calculateDelay(LocalTime attendanceTime) {
        long delay = Duration.between(LocalTime.now(), attendanceTime).toMillis();
        return delay >= 0 ? delay : TimeUnit.DAYS.toMillis(1) + delay; // 다음 날로 예약
    }

    private void sendAttendanceMessages(User user, Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms) {
        log.info("출근 메시지 전송: 사용자 {}", user.getNickname());
        List<String> messages = createMessages(user);
        messages.forEach(message -> sendMessage(user, message, preloadedChatRooms));
    }

    private List<String> createMessages(User user) {
        String nickname = user.getNickname();
        String startTime = user.getAttendanceTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = user.getAttendanceTime().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"));

        return List.of(
                String.format("안녕하세요 %s 인턴님! 오늘도 좋은 하루를 시작하기 위해 첫 업무를 안내드립니다.", nickname),
                String.format("업무: 침대 정리하기(%s ~ %s)", startTime, endTime),
                "업무 공간을 깔끔하게 유지하는 것은 업무 효율을 높이는 첫걸음입니다. 출근 준비를 하시고 깨끗한 환경에서 업무를 시작해 주세요! 출근 준비가 된 후 책상에 앉으신 다음에 업무 보고 부탁드립니다. 오늘도 멋진 하루 되시길 바랍니다!",
                "오늘도 좋은 하루 되세요!"
        );
    }


    private void sendMessage(User user, String message, Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms) {
        ChatRoom chatRoom = getOrCreateChatRoom(user, preloadedChatRooms);
        chatRepository.save(createChat(chatRoom, message));
    }

    private ChatRoom getOrCreateChatRoom(User user, Map<UUID, Map<EChatType, ChatRoom>> preloadedChatRooms) {
        return preloadedChatRooms
                .computeIfAbsent(user.getId(), id -> new HashMap<>())
                .computeIfAbsent(EChatType.FOLD, type -> createChatRoom(user));
    }

    private ChatRoom createChatRoom(User user) {
        return chatRoomRepository.save(
                ChatRoom.builder()
                        .user(user)
                        .chatType(EChatType.FOLD)
                        .title("일상회복팀 리부트대리")
                        .build()
        );
    }

    private Chat createChat(ChatRoom chatRoom, String message) {
        return Chat.builder()
                .chatRoomId(chatRoom.getId())
                .responseContent(message)
                .speaker(ESpeaker.AI)
                .isRead(false)
                .build();
    }

    private void logScheduledTasks() {
        scheduledTasks.forEach((userId, task) ->
                log.info("사용자 ID: {}, 상태: {}", userId, task.isCancelled() ? "취소됨" : "활성화됨")
        );
        if (scheduledTasks.isEmpty()) {
            log.info("예약된 작업이 없습니다.");
        }
    }
}
