package com.goormthom.danpoong.reboot.usecase.chat;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.ReadChatRoomResponseDto;
import java.util.List;
import java.util.UUID;

@UseCase
public interface ReadChatRoomUseCase {
    List<ReadChatRoomResponseDto> execute(UUID userId);
}
