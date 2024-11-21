package com.goormthom.danpoong.reboot.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record AllAnalysisResponseDto(
        @Schema(description = "일지 분석 결과", implementation = JournalResponseDto.class)
        JournalResponseDto journalResponseDto,
        @ArraySchema(schema = @Schema(implementation = MissionListResponseDto.class))
        List<MissionListResponseDto> missionListResponseDto,
        @Schema(description = "남은 기간", implementation = RemainPeriodResponseDto.class)
        RemainPeriodResponseDto remainPeriodResponseDto
) {
}
