package com.goormthom.danpoong.reboot.service.user;

import com.goormthom.danpoong.reboot.domain.Meal;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import com.goormthom.danpoong.reboot.dto.request.CreateMealRequestDto;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.MealRepository;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.batch.DynamicTaskScheduler;
import com.goormthom.danpoong.reboot.usecase.user.CreateRegisterUseCase;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRegisterService implements CreateRegisterUseCase {
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final DynamicTaskScheduler dynamicTaskScheduler;

    @Override
    public Boolean execute(CreateRegisterRequestDto createRegisterRequestDto, UUID userId) {
        User user = findUserById(userId);
        user.updateRegister(createRegisterRequestDto);

        Set<EMealTime> requestedMealTimes = extractRequestedMealTimes(createRegisterRequestDto);
        List<Meal> existingMeals = mealRepository.findByUser(user);

        List<Meal> mealsToDelete = findMealsToDelete(existingMeals, requestedMealTimes);
        List<Meal> mealsToAdd = findMealsToAdd(user, existingMeals, requestedMealTimes);

        processMealChanges(mealsToDelete, mealsToAdd);

        dynamicTaskScheduler.scheduleSingleUserTask(user);

        return true;
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }

    private Set<EMealTime> extractRequestedMealTimes(CreateRegisterRequestDto createRegisterRequestDto) {
        return createRegisterRequestDto.mealTimeList().stream()
                .map(CreateMealRequestDto::mealTime)
                .collect(Collectors.toSet());
    }

    private List<Meal> findMealsToDelete(List<Meal> existingMeals, Set<EMealTime> requestedMealTimes) {
        return existingMeals.stream()
                .filter(existingMeal -> !requestedMealTimes.contains(existingMeal.getMealTime()))
                .toList();
    }

    private List<Meal> findMealsToAdd(User user, List<Meal> existingMeals, Set<EMealTime> requestedMealTimes) {
        Set<EMealTime> existingMealTimes = existingMeals.stream()
                .map(Meal::getMealTime)
                .collect(Collectors.toSet());

        return requestedMealTimes.stream()
                .filter(requestedMealTime -> !existingMealTimes.contains(requestedMealTime))
                .map(requestedMealTime -> Meal.toEntity(user, requestedMealTime))
                .toList();
    }

    private void processMealChanges(List<Meal> mealsToDelete, List<Meal> mealsToAdd) {
        if (!mealsToDelete.isEmpty()) {
            mealRepository.deleteAll(mealsToDelete);
        }
        if (!mealsToAdd.isEmpty()) {
            mealRepository.saveAll(mealsToAdd);
        }
    }
}