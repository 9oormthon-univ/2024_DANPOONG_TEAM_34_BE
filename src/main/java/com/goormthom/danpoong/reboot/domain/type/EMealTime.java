package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EMealTime {
    MORNING("MORNING"),
    LUNCH("LUNCH"),
    DINNER("DINNER")
    ;

    private final String meat;

    public static EMealTime fromName(String meat) {
        return EMealTime.valueOf(meat.toUpperCase());
    }

    @Override
    public String toString() {
        return meat;
    }
}
