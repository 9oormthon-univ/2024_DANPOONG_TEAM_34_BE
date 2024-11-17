package com.goormthom.danpoong.reboot.service.oauth;

import com.goormthom.danpoong.reboot.usecase.oauth.RedirectToKakaoLoginUseCase;
import com.goormthom.danpoong.reboot.util.OAuth2Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectToKakaoLoginService implements RedirectToKakaoLoginUseCase {
    private final OAuth2Util oAuth2Util;

    @Override
    public String execute() {
        return oAuth2Util.getKakaoRedirectUrl();
    }
}
