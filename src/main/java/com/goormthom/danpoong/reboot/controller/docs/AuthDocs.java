package com.goormthom.danpoong.reboot.controller.docs;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ExceptionDto;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@Tag(name = "Auth Controller", description = "회원 인증 및 권한 관련 API를 제공합니다. 주로 토큰 재발급 및 회원 삭제와 같은 기능을 수행합니다.")
public interface AuthDocs {

    @Operation(
            summary = "액세스 토큰 재발급",
            description = """
                **사용 목적**:  
                만료된 액세스 토큰을 유효한 리프레시 토큰을 이용해 재발급합니다.  
                
                **요청 방법**:  
                - HTTP `POST` 메서드 사용
                - 요청 시 `Authorization` 헤더에 `Bearer <Refresh Token>` 형식으로 리프레시 토큰을 포함해야 합니다.
                
                **주요 사항**:  
                - 리프레시 토큰이 만료되었거나 유효하지 않은 경우, 401 에러가 반환됩니다.
                - 성공 시 새로운 액세스 토큰이 반환됩니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 액세스 토큰 재발급 성공. 
                        - 새로운 액세스 토큰과 리프레시 토큰이 응답으로 반환됩니다.
                        """,
                    content = @Content(schema = @Schema(implementation = JwtTokenDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        - `Expired Token`: 제공된 리프레시 토큰이 만료된 경우
                        - `Invalid Token`: 제공된 리프레시 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                        """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    ResponseDto<?> reissueToken(
            HttpServletRequest request
    );

    @Operation(
            summary = "회원 삭제",
            description = """
                **사용 목적**:  
                회원 계정을 영구적으로 삭제합니다.  
                
                **요청 방법**:  
                - HTTP `POST` 메서드 사용
                - 요청 시 `Authorization` 헤더에 액세스 토큰이 포함되어야 합니다.
                
                **주요 사항**:  
                - 요청 본문은 필요하지 않습니다.
                - 액세스 토큰이 만료되었거나 유효하지 않은 경우, 401 에러가 반환됩니다.
                - 성공적으로 처리되면 회원 계정이 삭제되고, 추가적인 데이터 요청은 처리되지 않습니다.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                        - 회원 삭제 성공. 
                        - 성공 시 응답 본문이 비어 있습니다.
                        """,
                    content = @Content(mediaType = "application/json", schema = @Schema(description = "성공 시 응답 본문이 비어 있습니다."))
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
    ResponseDto<?> withdrawal(
            @Parameter(hidden = true)
            @UserId UUID userId
    );
}