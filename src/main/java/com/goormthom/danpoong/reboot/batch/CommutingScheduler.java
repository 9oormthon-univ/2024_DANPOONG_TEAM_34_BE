package com.goormthom.danpoong.reboot.batch;

import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.usecase.scheduler.LeaveSchedulerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommutingScheduler {
    private final LeaveSchedulerUseCase leaveSchedulerUseCase;

    @Scheduled(cron = "0 0 20 * * *")
    public void leaveTask() {
        log.info("leaveTask Running");
        leaveSchedulerUseCase.execute(EChatType.LEAVE);
    }
}
