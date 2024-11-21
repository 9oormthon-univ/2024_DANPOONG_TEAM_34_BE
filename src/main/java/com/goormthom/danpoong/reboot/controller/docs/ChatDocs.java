package com.goormthom.danpoong.reboot.controller.docs;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.request.CreateChatRequestDto;
import com.goormthom.danpoong.reboot.dto.response.PromaDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatResponseDto;
import com.goormthom.danpoong.reboot.dto.response.ReadChatRoomResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Chat Controller", description = "채팅과 관련된 api제공합니다.")
public interface ChatDocs {

    @Operation(
            summary = "업무 보고",
            description = """
                   **사용 목적**:
                   사용자가 업부 보고를 할때 사진과 내용을 기반으로 답변을 반환합니다.
                   
                   **요청 방법**:
                   - HTTP `POST` 메소드 사용
                   - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                   
                    **주요 사항**
                    - 사용자 인증이 필요한 API입니다.
                    - 성공적으로 요청시에 AI의 응답이 반환됩니다.
                   """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            - AI응답 성공
                            - AI응답 반환
                            """,
                    content = @Content(schema = @Schema(implementation = PromaDto.class), mediaType = "application/json")

            )
    })
    ResponseDto<?> createChat(
            @Valid @RequestPart(value = "request") CreateChatRequestDto createChatRequestDto,
            @RequestPart(value = "image") MultipartFile file,
            @Parameter(hidden = true) @UserId UUID userId
    );

    @Operation(
            summary = "채팅방 리스트",
            description = """
                   **사용 목적**:
                   사용자가 채팅방 리스트를 조회할때 사용합니다. 
                   
                   **요청 방법**:
                   - HTTP `GET` 메소드 사용
                   - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                    
                    **주요 사항**
                    - 사용자 인증이 필요한 API입니다.
                    - 성공적으로 요청시에 채팅방 리스트가 반환됩니다.
                   """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            - 채팅방 리스트 출력
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadChatRoomResponseDto.class),
                            array = @ArraySchema(schema = @Schema(implementation = ReadChatRoomResponseDto.class))
                    )
            )
    })
    ResponseDto<?> getChatRooms(
            @Parameter(hidden = true)
            @UserId UUID userId
    );

    @Operation(
            summary = "채팅 내역 조회",
            description = """
                   **사용 목적**:
                   pathvarible를 통해서 chatRoomId를 받고 그 채팅방의 내용을 리스트로 뽑아줍니다.
                   
                   **요청 방법**:
                   - HTTP `GET` 메소드 사용
                   - 요청 시 `Authorization` 헤더에 `Bearer <Access Token>` 형식으로 액세스 토큰을 포함해야 합니다.
                   
                    **주요 사항**
                    - 사용자 인증이 필요한 API입니다.
                    - 답변을 AI와 USER 각각 리스트로 반환하는 api여서 사용시 반환 json을 주의깊게 봐주세요
                    - 성공적으로 요청시에 채팅내역리스트가 반환됩니다.
                   """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            - 사용자 채팅내역
                            - AI 채팅내역
                            """,

                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadChatResponseDto.class),
                            array = @ArraySchema(schema = @Schema(implementation = ReadChatResponseDto.class))
                    )
            )
    })
    ResponseDto<?> getChats(
            @Parameter(hidden = true)
            @UserId UUID userId,
            @PathVariable(value = "chatRoomId") Long chatRoomId
    );
}
