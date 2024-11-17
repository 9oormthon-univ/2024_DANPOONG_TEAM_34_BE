package com.goormthom.danpoong.reboot.usecase.auth;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;

@UseCase
public interface ReissueTokenUseCase {
    JwtTokenDto execute(String refreshToken);
}
