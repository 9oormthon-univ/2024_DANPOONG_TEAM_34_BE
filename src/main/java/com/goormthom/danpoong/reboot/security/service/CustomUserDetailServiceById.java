package com.goormthom.danpoong.reboot.security.service;

import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.security.info.UserPrincipal;
import com.goormthom.danpoong.reboot.security.usecase.LoadUserPrincipalByIdUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceById implements UserDetailsService, LoadUserPrincipalByIdUseCase {
    private final UserRepository userRepository;

    /* TODO: 만약 사용할 시 구현 필요이 필요하지만 사용하지 않으므로 주석 처리함 */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    @Override
    public UserDetails execute(UUID userId) {
        UserRepository.UserSecurityForm userSecurityForm = userRepository.findFormById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return UserPrincipal.create(userSecurityForm);
    }
}
