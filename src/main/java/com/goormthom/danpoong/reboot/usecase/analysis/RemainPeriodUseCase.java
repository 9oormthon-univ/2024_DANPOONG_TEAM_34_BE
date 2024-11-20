package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.RemainPeriodResponseDto;

import java.util.UUID;

@UseCase
public interface RemainPeriodUseCase {
    RemainPeriodResponseDto execute(UUID userId);
}
