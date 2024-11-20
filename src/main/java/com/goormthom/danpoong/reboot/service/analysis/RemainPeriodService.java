package com.goormthom.danpoong.reboot.service.analysis;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.response.RemainPeriodResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.analysis.RemainPeriodUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RemainPeriodService implements RemainPeriodUseCase {

    private final UserRepository userRepository;

    @Override
    public RemainPeriodResponseDto execute(UUID userId) {
        User user = findUserById(userId);

        LocalDate workStartTime = user.getWorkStartTime();
        LocalDate workEndTime = user.getWorkEndTime();
        LocalDate today = LocalDate.now();

        long contractPeriod = calculateDaysBetween(workStartTime, workEndTime);
        long remainPeriod = Math.max(calculateDaysBetween(today, workEndTime), 0);
        long progressPeriod = Math.min(calculateDaysBetween(workStartTime, today), contractPeriod);

        return RemainPeriodResponseDto.builder()
                .contractPeriod(contractPeriod)
                .remainPeriod(remainPeriod)
                .progressPeriod(progressPeriod)
                .build();
    }

    private long calculateDaysBetween(LocalDate start, LocalDate end) {
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}
