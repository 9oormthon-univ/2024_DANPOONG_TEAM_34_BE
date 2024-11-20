package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.response.JournalResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.JournalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JournalService implements JournalUseCase {
    private final UserRepository userRepository;

    @Override
    public JournalResponseDto execute(UUID userId) {
        User user = findUserById(userId);

        return JournalResponseDto.builder()
                .currentTime(LocalDate.now())
                .workStartTime(user.getWorkStartTime())
                .workEndTime(user.getWorkEndTime())
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}
