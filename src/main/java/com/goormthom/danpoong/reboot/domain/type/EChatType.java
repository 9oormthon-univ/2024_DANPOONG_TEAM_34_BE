package com.goormthom.danpoong.reboot.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum EChatType {
    FOLD("FOLD","출근하기", 1L), // 이불 정리 - 사용자 기상시간 - 기상시간 + 30분

    MORNING("MORNING", "아침 먹기", 2L), // 아침 - 09:00 - 10:00
    LUNCH("LUNCH","점심 먹기", 3L), // 점심 - 12:00 - 13:00
    DINNER("DINNER","저녁 먹기", 5L), // 저녁 - 17:00 - 20:00

    WALK("WALK","산책가기", 4L), // 걷기 - 15:00 - 16:00
    MARKET("MARKET","장보기", 4L), // 장보기 - 15:00 - 16:00
    PICTURE("PICTURE", "외부사진찍기", 4L), // 출사 - 15:00 - 16:00

    LEAVE("LEAVE","퇴근하기", 6L), // 퇴근 - 20:00 - 21:00
    ;

    private final String chatType;
    @Getter
    private final String description;

    private final Long weight;

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

    public Long weight() {
        return weight;
    }
}
