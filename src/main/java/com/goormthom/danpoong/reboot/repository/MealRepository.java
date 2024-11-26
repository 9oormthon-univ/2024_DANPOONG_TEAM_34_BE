package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.Meal;
import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByMealTime(EMealTime mealTime);
    List<Meal> findByUser(User user);
}
