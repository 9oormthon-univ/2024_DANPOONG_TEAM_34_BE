package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.JournalResponseDto;

import java.util.UUID;

@UseCase
public interface JournalUseCase {
    JournalResponseDto execute(UUID userId);
}
