package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.usecase.user.ReadUserUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final ReadUserUseCase readUserUseCase;

    @GetMapping("")
    public ResponseDto<?> readUser(
            @UserId UUID userId
            ) {
        return ResponseDto.ok(readUserUseCase.executeMono(userId));
    }
}
