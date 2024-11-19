package com.goormthom.danpoong.reboot.usecase.user;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.request.CreateOnBoardingRequestDto;
import java.util.UUID;

@UseCase
public interface CreateOnBoardingUseCase {
    Boolean execute(CreateOnBoardingRequestDto createOnBoardingRequestDto, UUID userId);
}
