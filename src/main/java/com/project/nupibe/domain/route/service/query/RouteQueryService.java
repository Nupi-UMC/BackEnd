package com.project.nupibe.domain.route.service.query;

import com.project.nupibe.domain.member.repository.MemberRouteRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.member.repository.RouteLikeRepository;
import com.project.nupibe.domain.route.converter.RouteConverter;
import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.dto.response.RoutePlacesResDTO;
import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.entity.RouteSearchQuery;
import com.project.nupibe.domain.route.exception.RouteErrorCode;
import com.project.nupibe.domain.route.exception.RouteException;
import com.project.nupibe.domain.route.repository.RouteRepository;
import com.project.nupibe.domain.store.converter.StoreConverter;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.entity.StoreSearchQuery;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteQueryService {

    private final RouteRepository routeRepository;
    private final RouteLikeRepository routeLikeRepository;
    private final StoreRepository storeRepository;
    private final MemberRouteRepository memberRouteRepository;


    public RouteDetailResDTO.RouteDetailResponse getRouteDetail(Long routeId, Long memberId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));

        boolean isLiked = false;
        boolean isBookmarked = false;

        if (memberId != null) {
            // 좋아요 여부 확인
            isLiked = routeLikeRepository.existsByMemberIdAndRouteId(memberId, routeId);

            // 북마크 여부 확인
            isBookmarked = memberRouteRepository.existsByMemberIdAndRouteId(memberId, routeId);
        }

        List<RouteStoreDTO> storeList = storeRepository.findStoresByRouteId(routeId);

        return RouteConverter.convertToRouteDetailDTO(route, isLiked, isBookmarked,  storeList);
    }

    public RoutePlacesResDTO.RoutePlacesResponse getRoutePlaces(Long routeId) {
        // 1. 경로 조회
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));

        // 2. 네이티브 쿼리 결과를 DTO로 매핑
        List<Object[]> results = storeRepository.findStoresWithCalculatedDistance(routeId);
        List<RouteStoreDTO> places = mapToRouteStoreDTO(results);

        // 3. 첫 번째 장소의 distance를 null로 처리
        if (!places.isEmpty()) {
            RouteStoreDTO firstPlace = places.get(0);
            places.set(0, firstPlace.withDistance("첫번째장소"));
        }

        // 4. 결과 반환
        return RouteConverter.convertToRoutePlacesDTO(route,places);
    }

    // 네이티브 쿼리 결과를 RouteStoreDTO로 매핑하는 메서드(PostGIS는 네이티브 쿼리로 작동)
    private List<RouteStoreDTO> mapToRouteStoreDTO(List<Object[]> results) {
        return results.stream()
                .map(result -> {
                    Long storeId = ((Number) result[0]).longValue();
                    String name = (String) result[1];
                    String location = (String) result[2];
                    String category = (String) result[3];
                    Integer orderIndex = ((Number) result[4]).intValue();
                    Double distance = result[5] != null ? ((Number) result[5]).doubleValue() / 1000.0 : null; // 킬로미터 변환
                    String formattedDistance = formatDistance(distance); // 거리 포맷팅
                    String image = (String) result[6];

                    return RouteStoreDTO.builder()
                            .storeId(storeId)
                            .name(name)
                            .location(location)
                            .category(category)
                            .orderIndex(orderIndex)
                            .distance(formattedDistance)
                            .image(image)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 거리 포맷팅 메서드
    private String formatDistance(Double distance) {
        return distance != null ? String.format("%.1f Km", distance) : null;
    }

    //경로 검색 조회
    public RouteDetailResDTO.RoutePageResponse getRoutesWithQuery(String query, float lat, float lng, String search, Long cursor, int offset){
        Pageable pageable = PageRequest.of(0, offset);
        Slice<Route> routes;

        String enumQuery;
        switch (query) {
            case "거리순":
                enumQuery = RouteSearchQuery.DISTANCE.name();
                break;
            case "북마크순":
                enumQuery = RouteSearchQuery.BOOKMARKNUM.name();
                break;
            case "추천순":
                enumQuery = RouteSearchQuery.RECOMMEND.name();
                break;
            default:
                throw new StoreException(StoreErrorCode.UNSUPPORTED_QUERY);
        }

        if (enumQuery.equals(RouteSearchQuery.DISTANCE.name())) {
            if (cursor.equals(0L)) {
                routes = routeRepository.findBySearchOrderByDistanceDesc(pageable, lat, lng, search);
            } else {
                routes = routeRepository.findBySearchOrderByDistanceWithCursor(cursor, pageable, lat, lng, search);
            }
        }
        else if (enumQuery.equals(RouteSearchQuery.BOOKMARKNUM.name())) {
            if (cursor.equals(0L)) {
                routes = routeRepository.findBySearchOrderByBOOKMARKNUMAscIdAsc(pageable, search);
            } else {
                routes = routeRepository.findBySearchOrderByBOOKMARKNUMWithCursor(cursor, pageable, search);
            }
        }
        else if (enumQuery.equals(RouteSearchQuery.RECOMMEND.name())) {
            if (cursor.equals(0L)) {
                routes = routeRepository.findBySearchOrderByLikeNumAscIdAsc(pageable, search);
            } else {
                routes = routeRepository.findBySearchOrderByLikeNumWithCursor(cursor, pageable, search);
            }
        }
        else {
            throw new RouteException(RouteErrorCode.UNSUPPORTED_QUERY);
        }

        //stores가 비어있는지 확인하고 빈 리스트일 경우 처리
        if (routes.isEmpty()) {
            return new RouteDetailResDTO.RoutePageResponse(new ArrayList<>(), false,0L); // 빈 리스트로 초기화
        }

        return RouteConverter.convertToRoutePageDTO(routes);
    }
}
