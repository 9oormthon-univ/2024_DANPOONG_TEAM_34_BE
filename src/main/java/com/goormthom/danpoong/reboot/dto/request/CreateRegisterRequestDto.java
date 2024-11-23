package com.goormthom.danpoong.reboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
@Schema(description = "근로계약서 | Req")
public record CreateRegisterRequestDto(

        @Schema(description = "근무 기간", example = "3")
        @NotNull(message = "근무 기간을 입력해주세요")
        Integer partTime, // 근무 기간

        @Schema(description = "출근 시간")
        @NotNull(message = "출근 시간을 입력하세요")
        LocalTime attendanceTime,

        @Schema(description = "식사 여부")
        List<CreateMealRequestDto> mealTimeList,

        @Schema(description = "외출 여부", example = "true")
        @NotNull(message = "외출 정보를 선택해주세요")
        Boolean isOutside
) {
}
