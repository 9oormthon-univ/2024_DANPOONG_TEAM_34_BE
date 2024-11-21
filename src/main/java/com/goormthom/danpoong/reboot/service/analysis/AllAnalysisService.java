package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.dto.response.AllAnalysisResponseDto;
import com.goormthom.danpoong.reboot.usecase.analysis.AllAnalysisUseCase;
import com.goormthom.danpoong.reboot.usecase.analysis.JournalUseCase;
import com.goormthom.danpoong.reboot.usecase.analysis.MissionListUseCase;
import com.goormthom.danpoong.reboot.usecase.analysis.RemainPeriodUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AllAnalysisService implements AllAnalysisUseCase {
    private final JournalUseCase journalUseCase;
    private final MissionListUseCase missionListUseCase;
    private final RemainPeriodUseCase remainPeriodUseCase;

    @Override
    public AllAnalysisResponseDto execute(UUID userId) {
        return AllAnalysisResponseDto.builder()
                .journalResponseDto(journalUseCase.execute(userId))
                .missionListResponseDto(missionListUseCase.execute(userId))
                .remainPeriodResponseDto(remainPeriodUseCase.execute(userId))
                .build();
    }
}
