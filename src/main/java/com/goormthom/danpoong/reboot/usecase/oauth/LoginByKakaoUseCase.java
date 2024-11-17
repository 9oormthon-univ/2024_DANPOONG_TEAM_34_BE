package com.goormthom.danpoong.reboot.usecase.oauth;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;

@UseCase
public interface LoginByKakaoUseCase {
    JwtTokenDto execute(String accessToken);
}
