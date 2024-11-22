package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EMissionStatus {
    SUCCESS("SUCCESS"), // O
    UNCLEAR("UNCLEAR"), // 세모
    FAIL("FAIL"),
    ;// X
    private final String missionStatus;

    public static EMissionStatus fromName(String missionStatus) {
        return EMissionStatus.valueOf(missionStatus.toUpperCase());
    }

    @Override
    public String toString() {
        return missionStatus;
    }
}
