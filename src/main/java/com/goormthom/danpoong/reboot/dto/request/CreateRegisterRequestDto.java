package com.goormthom.danpoong.reboot.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public record CreateRegisterRequestDto(
        @NotNull(message = "근무 기간을 입력해주세요")
        Integer partTime, // 근무 기간

        @NotNull(message = "출근 시간을 입력하세요")
        LocalTime attendanceTime,

        @NotNull(message = "첫 출근 날짜를 입력하세요.")
        LocalDate workStartTime,

        List<CreateMealRequestDto> mealTimeList,

        @NotNull(message = "외출 정보를 선택해주세요")
        Boolean isOutside
) {
}
