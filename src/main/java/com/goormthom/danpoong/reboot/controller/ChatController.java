package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.controller.docs.ChatDocs;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.request.CreateChatRequestDto;
import com.goormthom.danpoong.reboot.usecase.chat.CreateChatUseCase;
import com.goormthom.danpoong.reboot.usecase.chat.ReadChatRoomUseCase;
import com.goormthom.danpoong.reboot.usecase.chat.ReadChatUseCase;
import com.goormthom.danpoong.reboot.util.S3Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private final S3Util s3Util;
    private final CreateChatUseCase createChatUseCase;
    private final ReadChatRoomUseCase readChatRoomUseCase;
    private final ReadChatUseCase readChatUseCase;

    @PostMapping( "")
    public ResponseDto<?> createChat(
            @RequestParam("question") String question,
            @RequestParam("eChatType") EChatType eChatType,
            //@Valid  @RequestPart CreateChatRequestDto createChatRequestDto,
            @RequestPart(name = "image", required = false) MultipartFile file,
            @UserId UUID userId
    ) {
        return ResponseDto.ok(createChatUseCase.execute(question, eChatType, file, userId));
    }

    @GetMapping("")
    public ResponseDto<?> getChatRooms(
            @UserId UUID userId
    ) {
        return ResponseDto.ok(readChatRoomUseCase.execute(userId));
    }

    @GetMapping("/{chatRoomId}")
    public ResponseDto<?> getChats(
            @UserId UUID userId,
            @PathVariable(value = "chatRoomId") Long chatRoomId
    ) {
        return ResponseDto.ok(readChatUseCase.execute(userId, chatRoomId));
    }
}
