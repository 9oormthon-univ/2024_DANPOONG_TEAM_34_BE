package com.goormthom.danpoong.reboot.usecase.scheduler;

import com.goormthom.danpoong.reboot.annotation.UseCase;
import com.goormthom.danpoong.reboot.domain.type.EChatType;

@UseCase
public interface LeaveSchedulerUseCase {
    void execute(EChatType eChatType);
}
