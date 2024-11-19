package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.dto.request.CreateOnBoardingRequestDto;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
import com.goormthom.danpoong.reboot.usecase.user.CreateOnBoardingUseCase;
import com.goormthom.danpoong.reboot.usecase.user.CreateRegisterUseCase;
import com.goormthom.danpoong.reboot.usecase.user.ReadUserUseCase;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final ReadUserUseCase readUserUseCase;
    private final CreateOnBoardingUseCase createOnBoardingUseCase;
    private final CreateRegisterUseCase createRegisterUseCase;

    @GetMapping("")
    public ResponseDto<?> readUser(
            @UserId UUID userId
            ) {
        return ResponseDto.ok(readUserUseCase.executeMono(userId));
    }

    @PostMapping("/on-boarding")
    public ResponseDto<?> onBoardUser(
            @Valid @RequestBody CreateOnBoardingRequestDto userOnBoardingRequestDto,
            @UserId UUID userId
    ) {
        return ResponseDto.ok(createOnBoardingUseCase.execute(userOnBoardingRequestDto, userId));
    }

    @PostMapping("/register")
    public ResponseDto<?> registerUser(
            @Valid @RequestBody CreateRegisterRequestDto createRegisterRequestDto,
            @UserId UUID userId
    ) {
        return ResponseDto.ok(createRegisterUseCase.execute(createRegisterRequestDto, userId));
    }
}
