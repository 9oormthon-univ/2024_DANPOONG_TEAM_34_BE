package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EProvider {

    DEFAULT("DEFAULT"),
    KAKAO("KAKAO"),
    ;

    private final String provider;

    public static EProvider fromName(String provider) {
        return EProvider.valueOf(provider.toUpperCase());
    }

    @Override
    public String toString() {
        return provider;
    }
}
