package com.goormthom.danpoong.reboot.domain;

import com.goormthom.danpoong.reboot.domain.type.EProvider;
import com.goormthom.danpoong.reboot.domain.type.ERole;
import com.goormthom.danpoong.reboot.dto.request.CreateOnBoardingRequestDto;
import com.goormthom.danpoong.reboot.dto.request.CreateRegisterRequestDto;
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

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @Column(name = "motivation")
    private String motivation;

    @Column(name = "join_date")
    private LocalDate joinDate; // 가입 일자

    @Column(name = "work_start_time")
    private LocalDate workStartTime; // 근무시작 일자

    @Column(name = "work_end_time")
    private LocalDate workEndTime; // 근무 종료 일자

    @Column(name = "attendance_time")
    private LocalTime attendanceTime; // 출근 시간

    @Column(name = "is_outside")
    private Boolean isOutside; // 외근 여부

    //------------------------------------

    @Builder
    public User(
            String serialId,
            EProvider provider,
            ERole role,
            String nickname,
            String password,
            String email
    ) {
        this.serialId = serialId;
        this.provider = provider;
        this.role = role;
        this.nickname = nickname;
        this.password = password;
        this.refreshToken = null;
        this.email = email;
    }

    //------------------------------------

    public void updateOnboard(CreateOnBoardingRequestDto createOnBoardingRequestDto) {
        this.nickname = createOnBoardingRequestDto.name();
        this.nameEn = createOnBoardingRequestDto.nameEn();
        this.gender = createOnBoardingRequestDto.gender();
        this.birthDate = createOnBoardingRequestDto.birthday();
        this.motivation = createOnBoardingRequestDto.motivation();
    }

    public void updateRegister(CreateRegisterRequestDto createRegisterRequestDto) {
        this.joinDate = LocalDate.now();
        this.workStartTime = createRegisterRequestDto.workStartTime();
        this.workEndTime = createRegisterRequestDto.workStartTime().plusDays(createRegisterRequestDto.partTime());
        this.attendanceTime = createRegisterRequestDto.attendanceTime();
        this.isOutside = createRegisterRequestDto.isOutside();
    }
}
