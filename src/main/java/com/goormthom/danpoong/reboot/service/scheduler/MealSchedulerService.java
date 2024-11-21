package com.goormthom.danpoong.reboot.service.scheduler;

import com.goormthom.danpoong.reboot.domain.Chat;
import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.Meal;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import com.goormthom.danpoong.reboot.domain.type.ESpeaker;
import com.goormthom.danpoong.reboot.repository.ChatRepository;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.MealRepository;
import com.goormthom.danpoong.reboot.usecase.scheduler.MealSchedulerUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealSchedulerService implements MealSchedulerUseCase {

    private final ChatRoomRepository chatRoomRepository;
    private final MealRepository mealRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void execute(EChatType chatType) {
        List<Meal> meals = fetchMealsByChatType(chatType);
        Map<User, ChatRoom> userChatRoomMap = fetchUserChatRooms(meals, chatType);

        List<ChatRoom> newChatRooms = createNewChatRooms(meals, chatType, userChatRoomMap);
        chatRoomRepository.saveAll(newChatRooms);

        List<Chat> chatsToSave = createChatsForUsers(userChatRoomMap);
        chatRepository.saveAll(chatsToSave);
    }

    private List<Meal> fetchMealsByChatType(EChatType chatType) {
        return mealRepository.findByMealTime(EMealTime.valueOf(chatType.toString()));
    }

    private Map<User, ChatRoom> fetchUserChatRooms(List<Meal> meals, EChatType chatType) {
        return chatRoomRepository.findAllByUserInAndChatType(
                        meals.stream().map(Meal::getUser).toList(), chatType)
                .stream()
                .collect(Collectors.toMap(ChatRoom::getUser, chatRoom -> chatRoom));
    }

    private List<ChatRoom> createNewChatRooms(List<Meal> meals, EChatType chatType, Map<User, ChatRoom> userChatRoomMap) {
        return meals.stream()
                .map(Meal::getUser)
                .filter(user -> userChatRoomMap.get(user) == null && user.getWorkEndTime().isAfter(LocalDate.now()))
                .map(user -> {
                    ChatRoom newChatRoom = ChatRoom.builder()
                            .title("일상회복팀 리부트대리")
                            .user(user)
                            .chatType(chatType)
                            .build();
                    userChatRoomMap.put(user, newChatRoom);
                    return newChatRoom;
                })
                .toList();
    }

    private List<Chat> createChatsForUsers(Map<User, ChatRoom> userChatRoomMap) {
        return userChatRoomMap.values().stream()
                .flatMap(chatRoom -> generateChatMessages(chatRoom).stream()
                        .map(message -> createChat(chatRoom, message)))
                .toList();
    }

    private List<String> generateChatMessages(ChatRoom chatRoom) {
        String userName = chatRoom.getUser().getNickname();
        return switch (chatRoom.getChatType()) {
            case MORNING -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(09:00~10:00) 오늘 업무 안내드립니다. 아침시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
                    "오늘 하루도 화이팅입니다!"
            );
            case LUNCH -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(12:00~13:00) 오늘 업무 안내드립니다. 점심시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
                    "오늘 하루도 화이팅입니다!"
            );
            case DINNER -> List.of(
                    String.format("안녕하세요 %s님, 일상회복팀 리부트대리입니다.", userName),
                    "(17:00~20:00) 오늘 업무 안내드립니다. 저녁시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
                    "오늘 하루도 화이팅입니다!"
            );
            default -> throw new IllegalArgumentException("Unknown chat type: " + chatRoom.getChatType());
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
}
