package com.goormthom.danpoong.reboot.usecase.analysis;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.MissionListResponseDto;

import java.util.List;
import java.util.UUID;

@UseCase
public interface MissionListUseCase {
    List<MissionListResponseDto> execute(UUID userId);
}
