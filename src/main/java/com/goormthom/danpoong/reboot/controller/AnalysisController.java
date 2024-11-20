package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.controller.docs.AnalysisDocs;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.usecase.analysis.JournalUseCase;
import com.goormthom.danpoong.reboot.usecase.analysis.RemainPeriodUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController implements AnalysisDocs {

    private final RemainPeriodUseCase remainPeriodUseCase;
    private final JournalUseCase journalUseCase;

    @GetMapping("/remain-period")
    public ResponseDto<?> getRemainPeriod(@UserId UUID userId) {
        return ResponseDto.ok(remainPeriodUseCase.execute(userId));
    }

    @GetMapping("/journal")
    public ResponseDto<?> getJournal(@UserId UUID userId) {
        return ResponseDto.ok(journalUseCase.execute(userId));
    }
}
