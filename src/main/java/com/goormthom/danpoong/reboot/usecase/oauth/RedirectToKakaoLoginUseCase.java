package com.goormthom.danpoong.reboot.usecase.oauth;

import com.goormthom.danpoong.reboot.annotation.UseCase;

@UseCase
public interface RedirectToKakaoLoginUseCase {
    String execute();
}
