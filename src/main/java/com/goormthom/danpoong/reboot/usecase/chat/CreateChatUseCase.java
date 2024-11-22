package com.goormthom.danpoong.reboot.usecase.chat;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.dto.request.CreateChatRequestDto;
import com.goormthom.danpoong.reboot.dto.response.PromaDto;
import java.util.UUID;

@UseCase
public interface CreateChatUseCase {
    PromaDto execute(String question, EChatType eChatType, String imageUrl, UUID userId);
}
