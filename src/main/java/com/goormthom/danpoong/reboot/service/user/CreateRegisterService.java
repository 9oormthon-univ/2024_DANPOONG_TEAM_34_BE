package com.goormthom.danpoong.reboot.service.user;

import com.goormthom.danpoong.reboot.domain.Meal;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.MealRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.user.CreateRegisterUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRegisterService implements CreateRegisterUseCase {
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    @Override
    public Boolean execute(CreateRegisterRequestDto createRegisterRequestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        user.updateRegister(createRegisterRequestDto);

        createRegisterRequestDto.mealTimeList()
                .forEach(createMealRequestDto -> mealRepository.save(Meal.toEntity(user, createMealRequestDto.mealTime())));

        return true;
    }
}
