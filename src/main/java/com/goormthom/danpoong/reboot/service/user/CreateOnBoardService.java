package com.goormthom.danpoong.reboot.service.user;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.dto.request.CreateOnBoardingRequestDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.user.CreateOnBoardingUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOnBoardService implements CreateOnBoardingUseCase {
    private final UserRepository userRepository;

    @Override
    public Boolean execute(CreateOnBoardingRequestDto createOnBoardingRequestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        user.updateOnboard(createOnBoardingRequestDto);

        return true;
    }

}
