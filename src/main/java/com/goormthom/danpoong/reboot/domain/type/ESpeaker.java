package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ESpeaker {

    USER("USER"),
    AI("AI"),
    SCHEDULER("SCHEDULER"),
    ;

    private final String speakerName;

    public static EProvider fromName(String speakerName) {
        return EProvider.valueOf(speakerName.toUpperCase());
    }

    @Override
    public String toString() {
        return speakerName;
    }
}
