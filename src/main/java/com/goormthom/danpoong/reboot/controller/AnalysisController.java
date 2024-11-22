package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.controller.docs.AnalysisDocs;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.response.AllAnalysisResponseDto;
import com.goormthom.danpoong.reboot.usecase.analysis.*;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController implements AnalysisDocs {

    private final RemainPeriodUseCase remainPeriodUseCase;
    private final JournalUseCase journalUseCase;
    private final MissionListUseCase missionListUseCase;
    private final CalendarUseCase calendarUseCase;
    private final CalendarDetailUseCase calendarDetailUseCase;
    private final AllAnalysisUseCase allAnalysisUseCase;

    @GetMapping("/remain-period")
    public ResponseDto<?> getRemainPeriod(@UserId UUID userId) {
        return ResponseDto.ok(remainPeriodUseCase.execute(userId));
    }

    @GetMapping("/journal")
    public ResponseDto<?> getJournal(@UserId UUID userId) {
        return ResponseDto.ok(journalUseCase.execute(userId));
    }

    @GetMapping("/list")
    public ResponseDto<?> getMissionList(@UserId UUID userId) {
        return ResponseDto.ok(missionListUseCase.execute(userId));
    }

    @GetMapping("/calendar")
    public ResponseDto<?> getCalendar(@Parameter(hidden = true) @UserId UUID userId, @RequestParam(name = "groupType") Long groupType) {
        return ResponseDto.ok(calendarUseCase.execute(userId, groupType));
    }

    @GetMapping("/calendar-detail")
    public ResponseDto<?> getCalendarDetail(@UserId UUID userId, @RequestParam(name = "date") LocalDate date) {
        return ResponseDto.ok(calendarDetailUseCase.execute(userId, date));
    }
    @GetMapping("/all")
    public ResponseDto<?> getAllAnalysis(@UserId UUID userId) {
        return ResponseDto.ok(allAnalysisUseCase.execute(userId));
    }
}
