package com.goormthom.danpoong.reboot.service.scheduler;

import com.goormthom.danpoong.reboot.constant.Constants;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<User, ChatRoom> userChatRoomMap = fetchOrCreateChatRooms(meals, chatType);
        List<Chat> chatsToSave = generateChatsForUsers(userChatRoomMap);
        saveChats(chatsToSave);
    }

    private List<Meal> fetchMealsByChatType(EChatType chatType) {
        EMealTime mealTime = convertChatTypeToMealTime(chatType);
        return mealRepository.findByMealTime(mealTime);
    }

    private Map<User, ChatRoom> fetchOrCreateChatRooms(List<Meal> meals, EChatType chatType) {
        Map<User, ChatRoom> userChatRoomMap = new HashMap<>();
        List<User> users = extractUsersFromMeals(meals);

        for (User user : users) {
            ChatRoom chatRoom = chatRoomRepository.findByUserAndChatType(user, chatType)
                    .orElseGet(() -> createAndSaveChatRoom(user, chatType));
            userChatRoomMap.put(user, chatRoom);
        }

        return userChatRoomMap;
    }

    private ChatRoom createAndSaveChatRoom(User user, EChatType chatType) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .title(String.format(Constants.TEAM, chatType.chatTitle()))
                .user(user)
                .chatType(chatType)
                .build();
        return chatRoomRepository.save(newChatRoom);
    }

    private List<User> extractUsersFromMeals(List<Meal> meals) {
        return meals.stream()
                .map(Meal::getUser)
                .toList();
    }

    private List<Chat> generateChatsForUsers(Map<User, ChatRoom> userChatRoomMap) {
        return userChatRoomMap.values().stream()
                .flatMap(chatRoom -> createChatsForChatRoom(chatRoom).stream())
                .toList();
    }

    private void saveChats(List<Chat> chats) {
        chatRepository.saveAll(chats);
    }

    private List<Chat> createChatsForChatRoom(ChatRoom chatRoom) {
        List<String> messages = generateChatMessages(chatRoom);
        return messages.stream()
                .map(message -> createChat(chatRoom, message))
                .toList();
    }

    private List<String> generateChatMessages(ChatRoom chatRoom) {
        String userName = chatRoom.getUser().getNickname();
        return switch (chatRoom.getChatType()) {
            case MORNING -> List.of(
                    String.format("안녕하세요 %s님, %s입니다.", userName, chatRoom.getTitle()),
                    "(09:00~10:00) 오늘 업무 안내드립니다. 아침시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
                    "오늘 하루도 화이팅입니다!"
            );
            case LUNCH -> List.of(
                    String.format("안녕하세요 %s님, %s입니다.", userName, chatRoom.getTitle()),
                    "(12:00~13:00) 오늘 업무 안내드립니다. 점심시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
                    "오늘 하루도 화이팅입니다!"
            );
            case DINNER -> List.of(
                    String.format("안녕하세요 %s님, %s입니다.", userName, chatRoom.getTitle()),
                    "(20:50~21:30) 오늘 업무 안내드립니다. 저녁시간에 맞춰서 식사를 챙겨주세요. 업무 완료 후 간단한 사진과 함께 무엇을 먹었는지 보고 부탁드립니다!",
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

    private EMealTime convertChatTypeToMealTime(EChatType chatType) {
        return EMealTime.valueOf(chatType.toString());
    }
}