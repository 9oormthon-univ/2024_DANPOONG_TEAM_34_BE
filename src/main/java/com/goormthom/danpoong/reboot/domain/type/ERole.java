package com.goormthom.danpoong.reboot.domain.type;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ERole {
    USER("USER", "ROLE_USER"),
    ADMIN("ADMIN", "ROLE_ADMIN");

    private final String name;
    private final String securityName;

    public static ERole fromName(String name) {
        return Arrays.stream(ERole.values()).
                filter(role -> role.getName().equals(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException("No such role: " + name));
    }
}
