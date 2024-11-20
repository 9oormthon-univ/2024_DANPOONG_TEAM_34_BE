package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.ChatRoom;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.response.MissionListResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.ChatRoomRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.MissionListUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionListService implements MissionListUseCase {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public List<MissionListResponseDto> execute(UUID userId) {
        User user = findUserById(userId);

        return chatRoomRepository.findByUser(user)
                .stream()
                .map(this::mapToMissionDto)
                .toList();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }

    private MissionListResponseDto mapToMissionDto(ChatRoom chatRoom) {
        return MissionListResponseDto.builder()
                .mission(chatRoom.getChatType().name())
                .build();
    }
}
