package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EChatType {
    RISE("RISE"),
    FOLD("FOLD"),
    WASH("WASH"),

    MORNING("MORNING"),
    LUNCH("LUNCH"),
    DINNER("DINNER"),

    WALK("WALK"),
    MARKET("MARKET"),
    PICTURE("PICTURE"),

    LEAVE("LEAVE"),
    ;

    private final String chatType;

    public static EChatType fromName(String chatType) {
        return EChatType.valueOf(chatType.toUpperCase());
    }

    @Override
    public String toString() {
        return chatType;
    }
}
