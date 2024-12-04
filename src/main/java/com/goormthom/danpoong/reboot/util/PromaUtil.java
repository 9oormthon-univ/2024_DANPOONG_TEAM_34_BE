package com.goormthom.danpoong.reboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormthom.danpoong.reboot.constant.Constants;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.dto.response.PromaMissionDto;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PromaUtil {
    @Value("${proma.answer-url}")
    private String promaAnswerUrl;

    @Value("${proma.mission.accessToken}")
    private String tokenMealAccessToken;

    @Value("${proma.mission.secretKey}")
    private String tokenMealSecretKey;

    @Value("${proma.free.accessToken}")
    private String tokenFreeAccessToken;

    @Value("${proma.free.secretKey}")
    private String tokenFreeSecretKey;

    private final RestClient restClient = RestClient.create();

    public PromaMissionDto generateAnswer(String messageQuestion, String imageUrl, String email, EChatType chatType) {
        Map<String, Object> response;
        try {
            response = Objects.requireNonNull(restClient.post()
                    .uri(promaAnswerUrl))
                    .headers(httpHeaders -> {
                        httpHeaders.set("Content-Type", "application/json");
                    })
                    .body(Map.of(
                            "userLoginId", email+chatType,
                            "apiToken", tokenMealAccessToken,
                            "secretKey", tokenMealSecretKey,
                            "messageQuestion", Constants.MISSION + chatType.description() + "  " + messageQuestion,
                            "fileType", "image",
                            "messageFile", imageUrl
                    ))
                    .retrieve()
                    .toEntity(Map.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        Map<String, Object> result = (Map<String, Object>) response.get("responseDto");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree((String) result.get("messageAnswer"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        String answer = jsonNode.get("answer").asText();
        Boolean isComplete = jsonNode.get("isComplete").asBoolean();

        return PromaMissionDto.of(answer, isComplete);
    }

    public String generatorFreeChatAnswer(String messageQuestion, String email, EChatType chatType) {
        Map<String, Object> response;
        try {
            response = Objects.requireNonNull(restClient.post()
                            .uri(promaAnswerUrl))
                    .headers(httpHeaders -> {
                        httpHeaders.set("Content-Type", "application/json");
                    })
                    .body(Map.of(
                            "userLoginId", email+chatType,
                            "apiToken", tokenFreeAccessToken,
                            "secretKey", tokenFreeSecretKey,
                            "messageQuestion", messageQuestion,
                            "fileType", "",
                            "messageFile", ""
                    ))
                    .retrieve()
                    .toEntity(Map.class).getBody();
        } catch (Exception e) {
            System.err.println("asdfasdf 여기입니다. ");
            e.printStackTrace();
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        Map<String, Object> result = (Map<String, Object>) response.get("responseDto");

        return (String) result.get("messageAnswer");
    }


}
