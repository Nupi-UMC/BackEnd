package com.project.nupibe.domain.route.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KakaoNaviService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String API_URL = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";


    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    public KakaoNaviService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getOptimalRoute(String origin, String destination, List<Map<String,String >> waypoints) {
        try {
            // JSON 요청 본문 구성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("origin", Map.of("x", origin.split(",")[0], "y", origin.split(",")[1]));
            requestBody.put("destination", Map.of("x", destination.split(",")[0], "y", destination.split(",")[1]));

            // waypoints 처리 (옵션)
            if (waypoints != null && !waypoints.isEmpty()) {
                requestBody.put("waypoints", waypoints);
            }

            requestBody.put("priority", "RECOMMEND");
            requestBody.put("car_fuel", "GASOLINE");
            requestBody.put("car_hipass", false);
            requestBody.put("alternatives", false);
            requestBody.put("road_details", false);

            // HTTP 요청 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            // 카카오 네비 api 호출 및 응답 로깅
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            System.out.println("Kakao API Response: " + response);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Kakao Navi API", e);
        }
    }

    private Map<String, String> parseCoordinates(String coordinates) {
        String[] parts = coordinates.split(",");
        return Map.of("x", parts[0], "y", parts[1]);
    }

}
