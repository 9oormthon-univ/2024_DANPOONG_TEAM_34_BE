package com.goormthom.danpoong.reboot.constant;

import java.util.List;

public class Constants {
    public static String USER_ID_ATTRIBUTE_NAME = "USER_ID";
    public static String USER_ID_CLAIM_NAME = "uid";
    public static String USER_ROLE_CLAIM_NAME = "rol";
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String ON_BOARDING = "onboarding";
    public static String REGISTER = "register";
    public static String HOME = "home";
    public static String dirName = "reboot/";
    public static String MISSION = "미션은 ";
    public static String TEAM = "일상회복본부 %s팀";

    public static List<String> NO_NEED_AUTH_URLS = List.of(
            "/auth/reissue",
            "/oauth/login/kakao",
            "/oauth/login/kakao/callback",

            "/api-docs.html",
            "/api-docs/**",

            "/favicon.ico",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",

            "/api/health"
    );

    public static List<String> USER_URLS = List.of(
            "/**");

    public static List<String> ADMIN_URLS = List.of(
            "/admin/**");
}
