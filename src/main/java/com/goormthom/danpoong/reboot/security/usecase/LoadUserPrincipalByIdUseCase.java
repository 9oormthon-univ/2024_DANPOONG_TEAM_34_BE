package com.goormthom.danpoong.reboot.security.usecase;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;

@UseCase
public interface LoadUserPrincipalByIdUseCase {

    UserDetails execute(UUID userId);
}
