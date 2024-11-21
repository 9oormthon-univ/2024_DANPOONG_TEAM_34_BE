package com.goormthom.danpoong.reboot.usecase.chat;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import java.util.List;
import java.util.UUID;

@UseCase
public interface ReadChatUseCase {
    List<ReadChatResponseDto> execute(UUID userId, Long chatRoomId);
}
