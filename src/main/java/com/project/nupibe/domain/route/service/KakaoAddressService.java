package com.project.nupibe.domain.route.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoAddressService {

    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoAddressService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map<String, Double> getCoordinatesFromAddress(String address) {
        try {
            // 요청 URL 설정
            String requestUrl = API_URL + "?query=" + address;

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 응답에서 좌표 추출
            JsonNode documents = rootNode.path("documents");
            if (documents.isEmpty()) {
                throw new IllegalArgumentException("주소를 찾을 수 없습니다: " + address);
            }

            JsonNode location = documents.get(0);
            double longitude = location.path("x").asDouble();
            double latitude = location.path("y").asDouble();

            Map<String, Double> coordinates = new HashMap<>();
            coordinates.put("x", longitude);
            coordinates.put("y", latitude);

            return coordinates;

        } catch (Exception e) {
            throw new RuntimeException("카카오 주소 변환 API 호출 실패: " + address, e);
        }
    }
}
