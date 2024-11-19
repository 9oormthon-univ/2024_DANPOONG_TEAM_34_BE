package com.goormthom.danpoong.reboot.controller.docs;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ExceptionDto;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.request.CreateOnBoardingRequestDto;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
import com.goormthom.danpoong.reboot.dto.response.UserDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "User Controller", description = "회원 정보 및 관련 기능을 제공합니다.")
public interface UserDocs {

    @Operation(
            summary = "회원 닉네임 조회",
            description = """
                **사용 목적**: 
                회원의 고유 ID(UUID)를 기반으로 회원의 닉네임을 반환합니다.
                
                **주요 사항**: 
                - 요청 헤더에 유효한 `Authorization` 토큰이 필요합니다.
                - 유효하지 않은 토큰 사용 시 `401` 상태 코드가 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 닉네임 반환 성공",
                    content = @Content(schema = @Schema(implementation = UserDetailDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 토큰이 유효하지 않거나 올바르지 않은 형식인 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> readUser(
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "회원 추가 정보 입력",
            description = """
                **사용 목적**: 
                회원 가입 후 추가 정보를 입력받아 저장합니다.
                
                **요청 데이터 설명**: 
                - 추가 정보를 포함한 JSON 데이터를 요청 본문으로 전달합니다.
                - 유효성 검사가 수행되며, 입력 데이터가 올바르지 않을 경우 오류가 반환됩니다.
                
                **주요 사항**: 
                - 요청 헤더에 유효한 `Authorization` 토큰이 필요합니다.
                - 추가 정보는 `CreateOnBoardingRequestDto` 형식으로 전달되어야 합니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 추가 정보 입력 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 토큰이 유효하지 않거나 올바르지 않은 형식인 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> onBoardUser(
            @Parameter(description = "회원 추가 정보 Request DTO", required = true)
            @Valid @RequestBody CreateOnBoardingRequestDto userOnBoardingRequestDto,
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "근로계약서 작성",
            description = """
                **사용 목적**: 
                회원이 근로계약서를 작성하고 저장합니다.
                
                **요청 데이터 설명**: 
                - 근로계약서 내용을 포함한 JSON 데이터를 요청 본문으로 전달합니다.
                - 유효성 검사가 수행되며, 입력 데이터가 올바르지 않을 경우 오류가 반환됩니다.
                
                **주요 사항**: 
                - 요청 헤더에 유효한 `Authorization` 토큰이 필요합니다.
                - 계약서 정보는 `CreateRegisterRequestDto` 형식으로 전달되어야 합니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "근로계약서 작성 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 토큰이 유효하지 않거나 올바르지 않은 형식인 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> registerUser(
            @Parameter(description = "근로계약서 Request DTO", required = true)
            @Valid @RequestBody CreateRegisterRequestDto createRegisterRequestDto,
            @Parameter(hidden = true)
            @UserId UUID userId
    );
}
