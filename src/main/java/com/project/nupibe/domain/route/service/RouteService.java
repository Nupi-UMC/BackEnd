package com.project.nupibe.domain.route.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.route.dto.RouteCreateRequestDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final StoreRepository storeRepository;
    private final RouteStoreRepository routeStoreRepository;
    private final KakaoNaviService kakaoNaviService;
    private final MemberRepository memberRepository;

    public void createRoute(RouteCreateRequestDto requestDto) {
        // memberId를 사용하여 Member 객체 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // 1. Route 엔티티 생성 및 저장
        Route route = Route.builder()
                .routeName(requestDto.getRouteName())
                .content(requestDto.getContent())
                .location(requestDto.getLocation())
                .date(requestDto.getDate())
                .category(requestDto.getCategory())
                .member(member)
                .likeNum(0)
                .bookmarkNum(0)
                .build();

        routeRepository.save(route);

        // 2. Store 정보를 찾아서 RouteStore에 저장
        List<Store> storeList = new ArrayList<>();
        for (RouteCreateRequestDto.StoreDto storeDto : requestDto.getStores()) {
            Store store = storeRepository.findById(storeDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));

            storeList.add(store);

            RouteStore routeStore = RouteStore.builder()
                    .route(route)
                    .store(store)
                    .orderIndex(storeDto.getOrderIndex())
                    .build();

            routeStoreRepository.save(routeStore);
        }

        // 3. Kakao Navi API 호출을 위한 좌표 데이터 준비
        if (storeList.size() < 2) {
            throw new IllegalArgumentException("At least two stores are required for route creation.");
        }

        // 출발지 (origin)
        String origin = storeList.get(0).getLongitude() + "," + storeList.get(0).getLatitude();

        // 도착지 (destination)
        String destination = storeList.get(storeList.size() - 1).getLongitude() + "," + storeList.get(storeList.size() - 1).getLatitude();

        // 경유지 (waypoints)
        List<String> waypoints = new ArrayList<>();
        for (int i = 1; i < storeList.size() - 1; i++) {
            Store waypointStore = storeList.get(i);
            waypoints.add(String.format("{\"name\":\"store%d\",\"x\":%f,\"y\":%f}", i, waypointStore.getLongitude(), waypointStore.getLatitude()));
        }

        // waypoints를 JSON 배열 문자열로 변환
        String waypointsJson = "[" + String.join(",", waypoints) + "]";

        // 최적 경로 검색 요청
        kakaoNaviService.getOptimalRoute(origin, destination, waypointsJson);
    }

}
