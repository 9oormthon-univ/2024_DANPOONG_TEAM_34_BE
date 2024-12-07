package com.goormthom.danpoong.reboot.batch;

import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.usecase.scheduler.MealSchedulerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MealScheduler {
    private final MealSchedulerUseCase mealSchedulerUseCase;

    @Scheduled(cron = "0 0 9 * * *")
    public void morningTask() {
        log.info("morningTask Running");
        mealSchedulerUseCase.execute(EChatType.MORNING);
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void lunchTask() {
        log.info("lunchTask Running");
        mealSchedulerUseCase.execute(EChatType.LUNCH);
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void dinnerTask() {
        log.info("dinnerTask Running");
        mealSchedulerUseCase.execute(EChatType.DINNER);
    }
}
