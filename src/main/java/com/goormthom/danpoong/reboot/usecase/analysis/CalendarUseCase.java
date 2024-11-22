package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.CalendarResponseDto;

import java.util.UUID;

@UseCase
public interface CalendarUseCase {
    CalendarResponseDto execute(UUID userId, Long groupType);
}
