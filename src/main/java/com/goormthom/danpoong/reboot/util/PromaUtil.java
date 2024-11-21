package com.goormthom.danpoong.reboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormthom.danpoong.reboot.constant.Constants;
import com.goormthom.danpoong.reboot.domain.type.EChatType;
import com.goormthom.danpoong.reboot.dto.response.PromaDto;
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

    @Value("${proma.meal.accessToken}")
    private String tokenMealAccessToken;

    @Value("${proma.meal.secretKey}")
    private String tokenMealSecretKey;

    private final RestClient restClient = RestClient.create();

    public PromaDto generateAnswer(String messageQuestion, String imageUrl, String email, EChatType chatType) {
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
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
        System.err.println("asdfas");
        Map<String, Object> result = (Map<String, Object>) response.get("responseDto");
        System.err.println(result);
        //String answer = result.get("messageAnswer").toString();
        //Map<String, Object> answer1 = (Map<String, Object>)result.get("messageAnswer");

        //Map<String, Object> messageAnswer = (Map<String, Object>) result.get("messageAnswer");

// JSON 문자열 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree((String) result.get("messageAnswer"));
        } catch (JsonProcessingException e) {
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        String answer = jsonNode.get("answer").asText();
        Boolean isComplete = jsonNode.get("isComplete").asBoolean();

        return PromaDto.of(answer, isComplete);
    }
}
