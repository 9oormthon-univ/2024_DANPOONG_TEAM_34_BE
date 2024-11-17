package com.goormthom.danpoong.reboot.service.oauth;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EProvider;
import com.goormthom.danpoong.reboot.domain.type.ERole;
import com.goormthom.danpoong.reboot.dto.response.JwtTokenDto;
import com.goormthom.danpoong.reboot.repository.UserRepository;
import com.goormthom.danpoong.reboot.usecase.oauth.LoginByKakaoUseCase;
import com.goormthom.danpoong.reboot.util.JwtUtil;
import com.goormthom.danpoong.reboot.util.OAuth2Util;
import com.goormthom.danpoong.reboot.util.PasswordUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginByKakaoService implements LoginByKakaoUseCase {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final OAuth2Util oAuth2Util;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public JwtTokenDto execute(String accessToken) {
        Map<String, String> userInfo = oAuth2Util.getKakaoUserInformation(accessToken);

        String serialId = userInfo.get("id");

        UserRepository.UserSecurityForm userSecurityForm = userRepository.findFormBySerialIdAndProvider(serialId, EProvider.KAKAO)
                .orElseGet(() -> {
                    User user = userRepository.save(
                            User.builder()
                                    .serialId(serialId)
                                    .provider(EProvider.KAKAO)
                                    .role(ERole.USER)
                                    .nickname(userInfo.get("nickname"))
                                    .password(bCryptPasswordEncoder.encode(PasswordUtil.generateRandomPassword())).build()
                    );

                    return UserRepository.UserSecurityForm.of(user);
                });

        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(
                userSecurityForm.getId(),
                userSecurityForm.getRole()
        );

        userRepository.updateRefreshToken(userSecurityForm.getId(), jwtTokenDto.refreshToken());

        return jwtTokenDto;
    }
}
