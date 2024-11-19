package com.goormthom.danpoong.reboot.usecase.oauth;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;
import com.goormthom.danpoong.reboot.dto.response.LoginResponseDto;

@UseCase
public interface LoginByKakaoUseCase {
    LoginResponseDto execute(String accessToken);
}
