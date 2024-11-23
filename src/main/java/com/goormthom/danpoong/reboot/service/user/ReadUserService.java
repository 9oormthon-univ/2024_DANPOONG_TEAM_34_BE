package com.goormthom.danpoong.reboot.service.user;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.response.UserDetailDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.user.ReadUserUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadUserService implements ReadUserUseCase {
    private final UserRepository userRepository;

    @Override
    public UserDetailDto executeMono(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESOURCE));

        return UserDetailDto.builder()
                .name(user.getNickname())
                .nameEn(user.getNameEn())
                .email(user.getEmail())
                .build();
    }
}
