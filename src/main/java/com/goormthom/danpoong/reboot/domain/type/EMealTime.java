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

    private final String meal;

    public static EMealTime fromName(String meal) {
        return EMealTime.valueOf(meal.toUpperCase());
    }

    @Override
    public String toString() {
        return meal;
    }
}
