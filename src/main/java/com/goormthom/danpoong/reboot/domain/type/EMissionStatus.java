package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EMissionStatus {
    SUCCESS("SUCCESS", "완료"), // O
    UNCLEAR("UNCLEAR", "노력필요"), // 세모
    FAIL("FAIL", "업무불참"),
    ;// X
    private final String missionStatus;
    @Getter
    private final String description;

    public static EMissionStatus fromName(String missionStatus) {
        return EMissionStatus.valueOf(missionStatus.toUpperCase());
    }

    @Override
    public String toString() {
        return missionStatus;
    }

}
