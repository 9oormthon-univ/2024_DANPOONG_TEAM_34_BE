package com.goormthom.danpoong.reboot.service.auth;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.auth.WithdrawalUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawalService implements WithdrawalUseCase {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        userRepository.delete(user);
    }
}
