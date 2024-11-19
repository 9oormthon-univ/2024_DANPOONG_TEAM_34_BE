package com.goormthom.danpoong.reboot.controller;

import com.goormthom.danpoong.reboot.annotation.UserId;
import com.goormthom.danpoong.reboot.constant.Constants;
import com.goormthom.danpoong.reboot.controller.docs.AuthDocs;
import com.goormthom.danpoong.reboot.dto.common.ResponseDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import com.goormthom.danpoong.reboot.usecase.auth.ReissueTokenUseCase;
import com.goormthom.danpoong.reboot.usecase.auth.WithdrawalUseCase;
import com.goormthom.danpoong.reboot.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthDocs {
    private final ReissueTokenUseCase reissueTokenUseCase;
    private final WithdrawalUseCase withdrawalUseCase;


    @PostMapping("/reissue")
    public ResponseDto<?> reissueToken(
            HttpServletRequest request
    ) {
        String refreshToken = HeaderUtil.refineHeader(request, Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AUTHORIZATION_HEADER));

        return ResponseDto.ok(reissueTokenUseCase.execute(refreshToken));
    }

    @PostMapping("/withdrawal")
    public ResponseDto<?> withdrawal(
            @UserId UUID userId
    ) {
        withdrawalUseCase.execute(userId);

        return ResponseDto.ok(null);
    }

}
