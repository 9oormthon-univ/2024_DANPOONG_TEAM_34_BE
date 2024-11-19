package com.goormthom.danpoong.reboot.usecase.user;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
import java.util.UUID;

@UseCase
public interface CreateRegisterUseCase {
    Boolean execute(CreateRegisterRequestDto requestDto, UUID userId);
}
