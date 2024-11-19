package com.goormthom.danpoong.reboot.controller.docs;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ExceptionDto;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Tag(name = "Auth Controller", description = "회원 인증 및 권한 관련 API를 제공합니다. 주로 토큰 재발급 및 회원 삭제와 같은 기능을 수행합니다.")
public interface AuthDocs {

    @Operation(
            summary = "액세스 토큰 재발급",
            description = """
        만료된 액세스 토큰을 재발급합니다. 
        요청 시 `Authorization` 헤더에 `Bearer <Refresh Token>` 형식으로 리프레시 토큰을 포함해야 합니다.
        리프레시 토큰이 유효하다면 새로운 액세스 토큰을 반환합니다.
        """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "액세스 토큰 재발급 성공",
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
    @PostMapping("/reissue")
    ResponseDto<?> reissueToken(
            HttpServletRequest request
    );

    @Operation(
            summary = "회원 삭제",
            description = """
        회원 정보를 삭제합니다. 
        요청 시 사용자 식별을 위해 액세스 토큰을 `Authorization` 헤더에 포함해야 합니다.
        요청이 성공적으로 처리되면 계정이 영구적으로 삭제됩니다.
        """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 삭제 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(description = "성공 시에는 응답 본문이 비어 있습니다."))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                - `Expired Token`: 제공된 액세스 토큰이 만료된 경우
                - `Invalid Token`: 제공된 액세스 토큰이 유효하지 않거나 올바른 형식이 아닌 경우
                """,
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "요청한 회원이 존재하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class), mediaType = "application/json")
            )
    })
    @PostMapping("/withdrawal")
    ResponseDto<?> withdrawal(
            @UserId UUID userId
    );
}