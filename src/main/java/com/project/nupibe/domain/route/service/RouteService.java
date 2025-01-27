package com.project.nupibe.domain.route.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.entity.MemberRoute;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberRouteRepository;
import com.project.nupibe.domain.route.dto.RouteCreateRequestDto;
import com.project.nupibe.domain.route.dto.RouteResponseDto;
import com.project.nupibe.domain.route.dto.RouteStoreDto;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.entity.RouteStore;
import com.project.nupibe.domain.route.repository.RouteRepository;
import com.project.nupibe.domain.route.repository.RouteStoreRepository;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private static final double EPSILON = 0.0001;

    private final ObjectMapper objectMapper;
    private final RouteRepository routeRepository;
    private final StoreRepository storeRepository;
    private final RouteStoreRepository routeStoreRepository;
    private final KakaoNaviService kakaoNaviService;
    private final MemberRepository memberRepository;
    private final KakaoAddressService kakaoAddressService;
    private final MemberRouteRepository memberRouteRepository;

    public RouteResponseDto createRoute(RouteCreateRequestDto requestDto) {

        System.out.println("Received Route Create Request: " + requestDto);

        // 1. 멤버 조회 (실제로 존재하는 멤버인지)
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버의 ID입니다."));
        System.out.println("Member found: " + member.getId());

        // 1. storeId로 가게 조회 후 주소 가져오기
        List<Store> stores = requestDto.getStores().stream()
                .map(storeDto -> storeRepository.findById(storeDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + storeDto.getId())))
                .collect(Collectors.toList());

        System.out.println("Fetched Stores: " + stores);

        // 2. 주소를 카카오 API로 좌표 변환
        Map<Long, Map<String, Double>> storeCoordinates = new HashMap<>();
        for (Store store : stores) {
            System.out.println("Fetching coordinates for store: " + store.getName() + ", Address: " + store.getAddress());

            // 카카오 API를통해 좌표 조회하기
            Map<String, Double> coords = kakaoAddressService.getCoordinatesFromAddress(store.getAddress());

            if (coords == null || coords.isEmpty()) {
                System.out.println("Failed to get coordinates for: " + store.getAddress());
                throw new RuntimeException("주소로부터 좌표를 가져오지 못했습니다: " + store.getAddress());
            }
            // 기존 store의 좌표 업데이트
            store.setLatitude(coords.get("y").floatValue());
            store.setLongitude(coords.get("x").floatValue());

            // DB에 업데이트
            storeRepository.save(store);

            storeCoordinates.put(store.getId(), coords);
            System.out.println("Coordinates for store: " + store.getId() + " -> " + coords);
        }
        // 4. 최적 경로 요청을 위한 좌표 준비
        List<Map<String, Double>> coordinates = new ArrayList<>(storeCoordinates.values());

        System.out.println("Final Coordinates List: " + coordinates);

        // 3. 최단 경로 요청을 위한 좌표 준비
        String origin = coordinates.get(0).get("x") + "," + coordinates.get(0).get("y");
        String destination = coordinates.get(coordinates.size() - 1).get("x") + "," + coordinates.get(coordinates.size() - 1).get("y");

        List<Map<String, String>> waypoints = coordinates.subList(1, coordinates.size() - 1).stream()
                .map(coord -> Map.of("x", coord.get("x").toString(), "y", coord.get("y").toString()))
                .collect(Collectors.toList());

        System.out.println("Origin: " + origin + ", Destination: " + destination);
        System.out.println("Waypoints: " + waypoints);

        // 4. 카카오 네비 API 호출로 최적 경로 요청
        String response = kakaoNaviService.getOptimalRoute(origin, destination, waypoints);
        System.out.println("Kakao API Response: " + response);
        // 5. 응답 파싱 및 storeId 매칭
        List<RouteResponseDto.OptimalRoute> optimalRoutes = new ArrayList<>();
        try {

            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

            Map<String, Object> summary = (Map<String, Object>) ((List<Map<String, Object>>) responseMap.get("routes")).get(0).get("summary");

            // 최적화된 순서의 좌표들 추출
            List<Map<String, Object>> orderedCoordinates = new ArrayList<>();
            orderedCoordinates.add((Map<String, Object>) summary.get("origin"));
            orderedCoordinates.addAll((List<Map<String, Object>>) summary.get("waypoints"));
            orderedCoordinates.add((Map<String, Object>) summary.get("destination"));

            // 좌표를 storeid로 매핑

            for (Map<String, Object> point : orderedCoordinates) {
                double x = Double.parseDouble(point.get("x").toString());
                double y = Double.parseDouble(point.get("y").toString());

                System.out.println("Parsed Point: x=" + x + ", y=" + y);

                Store matchedStore = stores.stream()
                        .filter(store -> Math.abs(storeCoordinates.get(store.getId()).get("x") - x) < EPSILON &&
                                Math.abs(storeCoordinates.get(store.getId()).get("y") - y) < EPSILON)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No matching store found for coordinates"));

                optimalRoutes.add(new RouteResponseDto.OptimalRoute(
                        matchedStore.getId(), // Store ID
                        matchedStore.getName(),         // Store Name
                        x,                               // 경도
                        y                                // 위도
                ));

                System.out.println("Matched store with ID: " + matchedStore.getId());
            }


        } catch (Exception e) {
            System.err.println("Failed to process Kakao response: " + e.getMessage());
            throw new RuntimeException("Failed to process Kakao response", e);
        }

        // 6. 경로 저장 및 응답 반환
        Route route = Route.builder()
                .routeName(requestDto.getRouteName())
                .content(requestDto.getContent())
                .location(requestDto.getLocation())
                .date(requestDto.getDate())
                .category(requestDto.getCategory())
                .member(memberRepository.findById(requestDto.getMemberId()).orElseThrow())
                .build();

        routeRepository.save(route);
        System.out.println("Route saved with ID: " + route.getId());

        // 7. MemberRoute 테이블에 사용자-경로 관계 저장하기
        MemberRoute memberRoute=MemberRoute.builder()
                .member(member)
                .route(route)
                .build();
        memberRouteRepository.save(memberRoute);
        System.out.println("MemberRoute saved for member ID: " + member.getId());

        // 8. RouteStore 테이블에 경로-가게 관계 및 순서 저장
        int index=1;
        for (RouteResponseDto.OptimalRoute optimalRoute : optimalRoutes) {
            Store store = storeRepository.findById(Long.valueOf(optimalRoute.getStoreId()))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid store ID: " + optimalRoute.getStoreId()));


            RouteStore routeStore = RouteStore.builder()
                    .route(route)
                    .store(store)
                    .orderIndex(index++)
                    .build();
            routeStoreRepository.save(routeStore);
            System.out.println("RouteStore saved for store ID: " + store.getId() + " with order: " + (index - 1));
        }


        return new RouteResponseDto(
                route.getId(),                           // Long routeId
                route.getRouteName(),                     // String routeName
                route.getDate().toString(),                // String date (LocalDate -> String)
                optimalRoutes,                            // List<OptimalRoute> optimizedPath
                route.getCreatedAt(),                      // LocalDateTime createdAt
                route.getUpdatedAt()                       // LocalDateTime updatedAt
        );
    }
}










