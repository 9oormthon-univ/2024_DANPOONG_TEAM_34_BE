package com.goormthom.danpoong.reboot.controller.docs;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ExceptionDto;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.response.AllAnalysisResponseDto;
import com.goormthom.danpoong.reboot.dto.response.JournalResponseDto;
import com.goormthom.danpoong.reboot.dto.response.MissionListResponseDto;
import com.goormthom.danpoong.reboot.dto.response.RemainPeriodResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

@Tag(name = "Analysis Controller", description = "업무 일지 분석 관련 API를 제공합니다. 주로 업무 일지를 분석하는 기능을 수행합니다.")
public interface AnalysisDocs {

    @Operation(
            summary = "남은 계약 기간 조회",
            description = """
                **사용 목적**:  
                사용자의 계약이 만료되기까지 남은 기간을 조회합니다.  
                
                **요청 방법**:  
                - HTTP `GET` 메서드 사용
                - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                
                **주요 사항**:  
                - 사용자 인증이 필요한 API입니다.
                - 성공적으로 요청 시 계약 종료일까지의 기간이 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 남은 계약 기간 조회 성공.
                        - 남은 계약 기간(일 수)이 포함된 응답 데이터 반환.
                        """,
                    content = @Content(schema = @Schema(implementation = RemainPeriodResponseDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 액세스 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 액세스 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                        """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> getRemainPeriod(
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "근무 일지 조회",
            description = """
                **사용 목적**:  
                사용자의 근무 시작일, 근무 종료일, 현재 날짜를 반환합니다.
                
                **요청 방법**:  
                - HTTP `GET` 메서드 사용
                - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                
                **주요 사항**:  
                - 사용자 인증이 필요한 API입니다.
                - 성공적으로 요청 시 근무 시작일, 근무 종료일, 현재 날짜가 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 근무 시작일, 근무 종료일, 현재 날짜 조회 성공.
                        """,
                    content = @Content(schema = @Schema(implementation = JournalResponseDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 액세스 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 액세스 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                        """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> getJournal(
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "미션 목록 조회",
            description = """
                **사용 목적**:  
                사용자의 미션 목록을 조회합니다.
                
                **요청 방법**:  
                - HTTP `GET` 메서드 사용
                - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                
                **주요 사항**:  
                - 사용자 인증이 필요한 API입니다.
                - 성공적으로 요청 시 사용자의 미션 목록이 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 미션 목록 조회 성공.
                        - 사용자의 미션 목록이 포함된 응답 데이터 반환.
                        """,

                    content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MissionListResponseDto.class),
                    array = @ArraySchema(schema = @Schema(implementation = MissionListResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 액세스 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 액세스 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                        """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> getMissionList(
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "전체 분석 조회",
            description = """
                **사용 목적**:  
                사용자의 근무 일지, 미션 목록, 남은 계약 기간을 조회합니다.
                
                **요청 방법**:  
                - HTTP `GET` 메서드 사용
                - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                
                **주요 사항**:  
                - 사용자 인증이 필요한 API입니다.
                - 성공적으로 요청 시 사용자의 근무 일지, 미션 목록, 남은 계약 기간이 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 전체 분석 조회 성공.
                        - 사용자의 근무 일지, 미션 목록, 남은 계약 기간이 포함된 응답 데이터 반환.
                        """,
                    content = @Content(schema = @Schema(implementation = AllAnalysisResponseDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 액세스 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 액세스 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                        """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> getAllAnalysis(
            @Parameter(hidden = true)
            @UserId UUID userId
    );
}
