package com.goormthom.danpoong.reboot.usecase.chat;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatUserResponseDto;
import java.util.List;
import java.util.UUID;

@UseCase
public interface ReadChatUseCase {
    ReadChatResponseDto execute(UUID userId, Long chatRoomId);
}
