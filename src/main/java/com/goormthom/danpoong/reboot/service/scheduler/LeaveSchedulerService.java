package com.goormthom.danpoong.reboot.service.scheduler;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.scheduler.LeaveSchedulerUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveSchedulerService implements LeaveSchedulerUseCase {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void execute(EChatType eChatType) {
        List<User> activeUsers = findActiveUsers();
        Map<User, ChatRoom> existingChatRoomsMap = findExistingChatRoomsMap(activeUsers, eChatType);
        List<User> usersWithoutChatRooms = findUsersWithoutChatRooms(activeUsers, existingChatRoomsMap.keySet());

        List<ChatRoom> newChatRooms = createAndSaveNewChatRooms(usersWithoutChatRooms, eChatType);
        List<ChatRoom> allChatRooms = mergeChatRooms(existingChatRoomsMap, newChatRooms);

        saveChatsForAllRooms(allChatRooms);

        log.info("스케줄링 작업 완료: 기존 {}개의 채팅방, 신규 {}개의 채팅방 처리 완료",
                existingChatRoomsMap.size(), newChatRooms.size());
    }

    private List<User> findActiveUsers() {
        // 데이터베이스에서 필요한 사용자만 필터링
        return userRepository.findAll().stream()
                .filter(user -> user.getWorkEndTime().isAfter(LocalDate.now()))
                .toList();
    }

    private Map<User, ChatRoom> findExistingChatRoomsMap(List<User> users, EChatType eChatType) {
        return chatRoomRepository.findAllByUserInAndChatType(users, eChatType).stream()
                .collect(Collectors.toMap(ChatRoom::getUser, chatRoom -> chatRoom));
    }

    private List<User> findUsersWithoutChatRooms(List<User> users, Set<User> usersWithChatRooms) {
        return users.stream()
                .filter(user -> !usersWithChatRooms.contains(user))
                .toList();
    }

    private List<ChatRoom> createAndSaveNewChatRooms(List<User> usersWithoutChatRooms, EChatType eChatType) {
        List<ChatRoom> newChatRooms = usersWithoutChatRooms.stream()
                .map(user -> ChatRoom.builder()
                        .user(user)
                        .chatType(eChatType)
                        .title("일상회복팀 리부트대리")
                        .build())
                .toList();
        return chatRoomRepository.saveAll(newChatRooms);
    }

    private List<ChatRoom> mergeChatRooms(Map<User, ChatRoom> existingChatRoomsMap, List<ChatRoom> newChatRooms) {
        return Stream.concat(existingChatRoomsMap.values().stream(), newChatRooms.stream())
                .toList();
    }

    private void saveChatsForAllRooms(List<ChatRoom> chatRooms) {
        List<Chat> allChats = chatRooms.parallelStream()
                .flatMap(chatRoom -> generateMessages(chatRoom).stream()
                        .map(message -> createChat(chatRoom, message)))
                .toList();
        chatRepository.saveAll(allChats);

        log.info("총 {}개의 채팅 메시지 저장 완료.", allChats.size());
    }

    private List<String> generateMessages(ChatRoom chatRoom) {
        String userName = chatRoom.getUser().getNickname();
        return List.of(
                String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                "(20:00~21:00) 마지막 업무 안내드립니다. 퇴근 준비를 마치고 하루를 정리해보세요. 완료 후 간단한 사진과 함께 보고 부탁드립니다!",
                "오늘 하루도 고생 많으셨습니다!"
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
}
