package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
