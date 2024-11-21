package com.goormthom.danpoong.reboot.service.scheduler;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.scheduler.OutsideSchedulerUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutsideSchedulerService implements OutsideSchedulerUseCase {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    private static final List<EChatType> ECHAT_TYPES = List.of(EChatType.WALK, EChatType.PICTURE, EChatType.MARKET);

    @Override
    @Transactional
    public void execute() {
        List<User> activeUsers = findActiveUsers();
        List<Chat> chatsToSave = activeUsers.stream()
                .flatMap(user -> processUser(user).stream())
                .toList();

        saveChats(chatsToSave);
        log.info("외부활동 스케줄링 작업 완료: 총 {}명의 사용자 처리 완료", activeUsers.size());
    }

    private List<User> findActiveUsers() {
        return userRepository.findAll().stream()
                .filter(this::isActiveUser)
                .toList();
    }

    private boolean isActiveUser(User user) {
        return user.getWorkEndTime().isAfter(LocalDate.now()) && user.getIsOutside();
    }

    private List<Chat> processUser(User user) {
        EChatType chatType = assignRandomChatType();
        log.info("사용자 {}에게 할당된 EChatType: {}", user.getNickname(), chatType);

        ChatRoom chatRoom = findOrCreateChatRoom(user, chatType);
        return createChatsForChatRoom(chatRoom);
    }

    private EChatType assignRandomChatType() {
        return ECHAT_TYPES.get(new Random().nextInt(ECHAT_TYPES.size()));
    }

    private ChatRoom findOrCreateChatRoom(User user, EChatType chatType) {
        return chatRoomRepository.findByUserAndChatType(user, chatType)
                .orElseGet(() -> createAndSaveChatRoom(user, chatType));
    }

    private ChatRoom createAndSaveChatRoom(User user, EChatType chatType) {
        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .chatType(chatType)
                .title("일상회복팀 리부트대리")
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    private List<Chat> createChatsForChatRoom(ChatRoom chatRoom) {
        List<String> messages = generateMessages(chatRoom);
        return messages.stream()
                .map(message -> createChat(chatRoom, message))
                .toList();
    }

    private List<String> generateMessages(ChatRoom chatRoom) {
        String userName = chatRoom.getUser().getNickname();
        return switch (chatRoom.getChatType()) {
            case WALK -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(15:00~16:00) 산책을 하며 신선한 공기를 느껴보세요. 활동 완료 후 사진과 함께 느낀 점을 공유해주세요!",
                    "오늘 하루도 활기차게 보내시길 바랍니다!"
            );
            case PICTURE -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(15:00~16:00) 주제를 정해 사진을 찍어보세요. 찍은 사진을 공유하며 창의력을 발휘해보세요!",
                    "활동을 통해 다양한 시각을 발견하시길 바랍니다!"
            );
            case MARKET -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(15:00~16:00) 편의점이나 마트를 방문해 필요한 물품을 구매해보세요. 구매한 물품과 소감도 함께 공유 부탁드립니다!",
                    "즐거운 쇼핑 시간이 되시길 바랍니다!"
            );
            default -> throw new IllegalStateException("Unexpected value: " + chatRoom.getChatType());
        };
    }

    private Chat createChat(ChatRoom chatRoom, String message) {
        return Chat.builder()
                .chatRoomId(chatRoom.getId())
                .responseContent(message)
                .speaker(ESpeaker.AI)
                .isRead(false)
                .build();
    }

    private void saveChats(List<Chat> chatsToSave) {
        if (!chatsToSave.isEmpty()) {
            chatRepository.saveAll(chatsToSave);
        }
    }
}
