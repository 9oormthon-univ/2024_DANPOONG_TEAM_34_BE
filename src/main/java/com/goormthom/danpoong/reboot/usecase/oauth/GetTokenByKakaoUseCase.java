package com.goormthom.danpoong.reboot.usecase.oauth;

import com.goormthom.danpoong.reboot.annotation.UseCase;

@UseCase
public interface GetTokenByKakaoUseCase {
    String execute(String authorizationCode);
}
