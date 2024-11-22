package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.CalendarDetailResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UseCase
public interface CalendarDetailUseCase {
    List<CalendarDetailResponseDto> execute(UUID userId, LocalDate date);
}
