package com.goormthom.danpoong.reboot.util;

import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OAuth2Util {
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String KAKAO_AUTHORIZATION_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USERINFO_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    private final RestClient restClient = RestClient.create();

    public String getKakaoRedirectUrl() {
        return KAKAO_AUTHORIZATION_URL
                + "?client_id=" + KAKAO_CLIENT_ID +
                "&redirect_uri=" + KAKAO_REDIRECT_URL +
                "&response_type=code";
    }

    public String getKakaoAccessToken(String authorizationCode) {
        Map<String, Object> response;

        try {
            response = Objects.requireNonNull(restClient.post()
                    .uri(KAKAO_TOKEN_URL)
                    .headers(httpHeaders -> {
                        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    })
                    .body("grant_type=authorization_code&client_id=" + KAKAO_CLIENT_ID + "&client_secret=" + KAKAO_CLIENT_SECRET + "&code=" + authorizationCode)
                    .retrieve()
                    .toEntity(Map.class).getBody());
        } catch (Exception e) {
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        return response.get("access_token").toString();
    }

    public Map<String, String> getKakaoUserInformation(String accessToken) {
        Map<String, Object> response;

        try {
            response = Objects.requireNonNull(restClient.post()
                    .uri(KAKAO_USERINFO_URL)
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(accessToken);
                        httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    })
                    .retrieve()
                    .toEntity(Map.class).getBody());
        } catch (Exception e) {
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");

        String id = response.get("id").toString();
        String nickname = profile.get("nickname");
        String email = kakaoAccount.get("email").toString();

        if (response.get("id") == null || response.get("properties") == null)
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);

        return Map.of(
                "id", id,
                "nickname", nickname,
                "email", email
        );
    }
}
