package com.goormthom.danpoong.reboot.usecase.user;

import com.goormthom.danpoong.reboot.dto.response.UserDetailDto;
import java.util.UUID;
import com.goormthom.danpoong.reboot.annotation.UseCase;

@UseCase
public interface ReadUserUseCase {
    UserDetailDto executeMono(UUID userId);
}
