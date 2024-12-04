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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRegisterService implements CreateRegisterUseCase {
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final DynamicTaskScheduler dynamicTaskScheduler;

    @Override
    @Transactional
    public Boolean execute(CreateRegisterRequestDto createRegisterRequestDto, UUID userId) {
        log.info("Starting CreateRegisterService.execute with userId: {}", userId);

        try {
            // 유저 조회
            User user = findUserById(userId);
            log.info("Found user: {}", user);

            // 유저 정보 업데이트
            user.updateRegister(createRegisterRequestDto);
            log.info("Updated user register information: {}", createRegisterRequestDto);

            // 요청받은 식사 시간 추출
            Set<EMealTime> requestedMealTimes = extractRequestedMealTimes(createRegisterRequestDto);
            log.info("Requested meal times: {}", requestedMealTimes);

            // 기존 식사 데이터 조회
            List<Meal> existingMeals = mealRepository.findByUser(user);
            log.info("Existing meals: {}", existingMeals);

            // 삭제할 식사 데이터와 추가할 식사 데이터 구분
            List<Meal> mealsToDelete = findMealsToDelete(existingMeals, requestedMealTimes);
            List<Meal> mealsToAdd = findMealsToAdd(user, existingMeals, requestedMealTimes);
            log.info("Meals to delete: {}", mealsToDelete);
            log.info("Meals to add: {}", mealsToAdd);

            // 데이터베이스 변경 처리
            processMealChanges(mealsToDelete, mealsToAdd);

            // 동적 스케줄러 작업 실행
            dynamicTaskScheduler.scheduleSingleUserTask(user);
            log.info("Task successfully scheduled for user: {}", user.getId());

            return true;
        } catch (Exception e) {
            log.error("Error occurred in CreateRegisterService.execute: ", e);
            throw e; // 예외를 다시 던져 트랜잭션 롤백 유도
        }
    }

    @Transactional
    protected User findUserById(UUID userId) {
        log.info("Finding user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for ID: {}", userId);
                    return new CommonException(ErrorCode.NOT_FOUND_USER);
                });
    }

    private Set<EMealTime> extractRequestedMealTimes(CreateRegisterRequestDto createRegisterRequestDto) {
        log.info("Extracting meal times from request DTO");
        return createRegisterRequestDto.mealTimeList().stream()
                .map(CreateMealRequestDto::mealTime)
                .collect(Collectors.toSet());
    }

    private List<Meal> findMealsToDelete(List<Meal> existingMeals, Set<EMealTime> requestedMealTimes) {
        log.info("Finding meals to delete...");
        return existingMeals.stream()
                .filter(existingMeal -> !requestedMealTimes.contains(existingMeal.getMealTime()))
                .toList();
    }

    private List<Meal> findMealsToAdd(User user, List<Meal> existingMeals, Set<EMealTime> requestedMealTimes) {
        log.info("Finding meals to add...");
        Set<EMealTime> existingMealTimes = existingMeals.stream()
                .map(Meal::getMealTime)
                .collect(Collectors.toSet());

        return requestedMealTimes.stream()
                .filter(requestedMealTime -> !existingMealTimes.contains(requestedMealTime))
                .map(requestedMealTime -> {
                    Meal meal = Meal.toEntity(user, requestedMealTime);
                    log.info("Prepared new meal entity: {}", meal);
                    return meal;
                })
                .toList();
    }

    private void processMealChanges(List<Meal> mealsToDelete, List<Meal> mealsToAdd) {
        log.info("Processing meal changes...");
        try {
            if (!mealsToDelete.isEmpty()) {
                log.info("Deleting meals: {}", mealsToDelete);
                mealRepository.deleteAll(mealsToDelete);
            }
            if (!mealsToAdd.isEmpty()) {
                log.info("Saving meals: {}", mealsToAdd);
                mealRepository.saveAll(mealsToAdd);
            }
        } catch (Exception e) {
            log.error("Error during meal changes: ", e);
        }
    }
}
