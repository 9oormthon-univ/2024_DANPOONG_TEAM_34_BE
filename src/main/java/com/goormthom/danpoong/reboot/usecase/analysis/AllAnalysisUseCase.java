package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.AllAnalysisResponseDto;

import java.util.UUID;

@UseCase
public interface AllAnalysisUseCase {
    AllAnalysisResponseDto execute(UUID userId);
}
