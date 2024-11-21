package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum EChatType {
    RISE("RISE","기상하기"), // 기상
    FOLD("FOLD","이불정리하기"), // 이불 정리
    WASH("WASH", "씻기"), // 씻기

    MORNING("MORNING", "아침 먹기"), // 아침
    LUNCH("LUNCH","점심 먹기"), // 점심
    DINNER("DINNER","저녁 먹기"), // 저녁

    WALK("WALK","산책가기"), // 걷기
    MARKET("MARKET","장보기"), // 장보기
    PICTURE("PICTURE", "외부사진찍기"), // 출사

    LEAVE("LEAVE","퇴근하기"), // 퇴근
    ;

    private final String chatType;
    @Getter
    private final String description;

    public static EChatType fromName(String chatType) {
        return EChatType.valueOf(chatType.toUpperCase());
    }

    @Override
    public String toString() {
        return chatType;
    }

    public String description() {
        return description;
    }

}
