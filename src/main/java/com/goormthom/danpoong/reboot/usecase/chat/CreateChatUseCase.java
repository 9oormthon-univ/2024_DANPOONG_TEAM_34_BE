package com.goormthom.danpoong.reboot.usecase.chat;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public interface CreateChatUseCase {
    ReadChatResponseDto execute(String question, EChatType eChatType, MultipartFile file, UUID userId);
}
