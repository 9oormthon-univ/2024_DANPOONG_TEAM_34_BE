package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.EMealTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "meals")
public class Meal {
    @Id
    @Column(name = "meal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "meal_time")
    @Enumerated(EnumType.STRING)
    private EMealTime mealTime;

    @Builder
    public Meal(User user, EMealTime mealTime) {
        this.user = user;
        this.mealTime = mealTime;
    }

    public static Meal toEntity(User user, EMealTime mealTime) {
        return Meal.builder()
                .mealTime(mealTime)
                .user(user)
                .build();
    }

    public void updateMeal(EMealTime mealTime) {
        this.mealTime = mealTime;
    }
}
