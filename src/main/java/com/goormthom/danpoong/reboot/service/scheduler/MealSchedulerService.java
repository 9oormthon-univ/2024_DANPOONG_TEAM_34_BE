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
        Map<User, ChatRoom> userChatRoomMap = fetchExistingUserChatRooms(meals, chatType);

        List<ChatRoom> newChatRooms = generateNewChatRooms(meals, chatType, userChatRoomMap);
        saveNewChatRooms(newChatRooms);

        List<Chat> chatsToSave = generateChatsForUsers(userChatRoomMap);
        saveChats(chatsToSave);
    }

    private List<Meal> fetchMealsByChatType(EChatType chatType) {
        EMealTime mealTime = convertChatTypeToMealTime(chatType);
        return mealRepository.findByMealTime(mealTime);
    }

    private Map<User, ChatRoom> fetchExistingUserChatRooms(List<Meal> meals, EChatType chatType) {
        List<User> users = extractUsersFromMeals(meals);
        return findChatRoomsByUsersAndType(users, chatType);
    }

    private List<ChatRoom> generateNewChatRooms(List<Meal> meals, EChatType chatType, Map<User, ChatRoom> userChatRoomMap) {
        List<User> usersNeedingChatRooms = findUsersWithoutChatRooms(meals, userChatRoomMap);
        return createChatRooms(usersNeedingChatRooms, chatType, userChatRoomMap);
    }

    private void saveNewChatRooms(List<ChatRoom> chatRooms) {
        chatRoomRepository.saveAll(chatRooms);
    }

    private List<Chat> generateChatsForUsers(Map<User, ChatRoom> userChatRoomMap) {
        return userChatRoomMap.values().stream()
                .flatMap(chatRoom -> createChatsForChatRoom(chatRoom).stream())
                .toList();
    }

    private void saveChats(List<Chat> chats) {
        chatRepository.saveAll(chats);
    }

    private EMealTime convertChatTypeToMealTime(EChatType chatType) {
        return EMealTime.valueOf(chatType.toString());
    }

    private List<User> extractUsersFromMeals(List<Meal> meals) {
        return meals.stream()
                .map(Meal::getUser)
                .toList();
    }

    private Map<User, ChatRoom> findChatRoomsByUsersAndType(List<User> users, EChatType chatType) {
        return chatRoomRepository.findAllByUserInAndChatType(users, chatType).stream()
                .collect(Collectors.toMap(ChatRoom::getUser, chatRoom -> chatRoom));
    }

    private List<User> findUsersWithoutChatRooms(List<Meal> meals, Map<User, ChatRoom> userChatRoomMap) {
        return meals.stream()
                .map(Meal::getUser)
                .filter(user -> isEligibleForChatRoom(user, userChatRoomMap))
                .toList();
    }

    private boolean isEligibleForChatRoom(User user, Map<User, ChatRoom> userChatRoomMap) {
        return userChatRoomMap.get(user) == null && user.getWorkEndTime().isAfter(LocalDate.now());
    }

    private List<ChatRoom> createChatRooms(List<User> users, EChatType chatType, Map<User, ChatRoom> userChatRoomMap) {
        return users.stream()
                .map(user -> createChatRoom(user, chatType, userChatRoomMap))
                .toList();
    }

    private ChatRoom createChatRoom(User user, EChatType chatType, Map<User, ChatRoom> userChatRoomMap) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .title(String.format(Constants.TEAM,chatType.chatTitle()))
                .user(user)
                .chatType(chatType)
                .build();
        userChatRoomMap.put(user, newChatRoom);
        return newChatRoom;
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

