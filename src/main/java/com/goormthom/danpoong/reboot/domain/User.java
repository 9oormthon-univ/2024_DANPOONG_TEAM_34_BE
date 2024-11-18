package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.EProvider;
import com.goormthom.danpoong.reboot.domain.type.ERole;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial_id", "provider"})
})
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "serial_id", nullable = false)
    private String serialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private EProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @Column(name = "refresh_Token")
    private String refreshToken;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name =  "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Lob
    @Column(name = "motivation", nullable = false)
    private String motivation;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    @Column(name = "attendance_time", nullable = false)
    private LocalTime attendanceTime;

    @Column(name = "is_outside", nullable = false)
    private Boolean isOutside;


    @Builder
    public User(
            String serialId,
            EProvider provider,
            ERole role,
            String nickname,
            String password
    ) {
        this.serialId = serialId;
        this.provider = provider;
        this.role = role;
        this.nickname = nickname;
        this.password = password;
        this.refreshToken = null;
    }
}
