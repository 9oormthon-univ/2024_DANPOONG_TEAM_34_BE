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

        LocalDate workStartTime = user.getWorkStartTime(); // 시작 일자
        LocalDate workEndTime = user.getWorkEndTime();     // 종료 일자
        LocalDate today = LocalDate.now();                // 현재 일자

        // 계약 기간 계산
        long contractPeriod = Duration.between(workStartTime.atStartOfDay(), workEndTime.atStartOfDay()).toDays();

        // 남은 기간 계산
        long remainPeriod = Duration.between(today.atStartOfDay(), workEndTime.atStartOfDay()).toDays();
        remainPeriod = Math.max(remainPeriod, 0); // 남은 기간이 음수가 되지 않도록

        // 진행된 기간 계산
        long progressPeriod = Duration.between(workStartTime.atStartOfDay(), today.atStartOfDay()).toDays();
        progressPeriod = Math.min(progressPeriod, contractPeriod); // 계약 기간보다 크지 않도록

        return RemainPeriodResponseDto.builder()
                .contractPeriod(contractPeriod)
                .remainPeriod(remainPeriod)
                .progressPeriod(progressPeriod)
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }
}
