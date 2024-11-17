package com.goormthom.danpoong.reboot.usecase.auth;

import java.util.UUID;
import com.goormthom.danpoong.reboot.annotation.UseCase;

@UseCase
public interface WithdrawalUseCase {
    void execute(UUID userId);
}
