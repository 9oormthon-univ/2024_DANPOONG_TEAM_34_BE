package com.goormthom.danpoong.reboot.batch;

import com.goormthom.danpoong.reboot.usecase.scheduler.OutsideSchedulerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutsideScheduler {
    private final OutsideSchedulerUseCase outsideSchedulerUseCase;

    @Scheduled(cron = "0 0 14 * * *")
    public void outsideTask() {
        log.info("outsideTask Running");
        outsideSchedulerUseCase.execute();
    }
}
