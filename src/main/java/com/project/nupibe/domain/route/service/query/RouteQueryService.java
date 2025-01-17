package com.project.nupibe.domain.route.service.query;

import com.project.nupibe.domain.route.converter.RouteConverter;
import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.dto.response.RoutePlacesResDTO;
import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.exception.RouteErrorCode;
import com.project.nupibe.domain.route.exception.RouteException;
import com.project.nupibe.domain.route.repository.RouteRepository;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteQueryService {

    private final RouteRepository routeRepository;
    private final StoreRepository storeRepository;

    public RouteDetailResDTO.RouteDetailResponse getRouteDetail(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));

        List<RouteStoreDTO> storeList = storeRepository.findStoresByRouteId(routeId);

        return RouteConverter.convertToRouteDetailDTO(route, storeList);
    }

    public RoutePlacesResDTO.RoutePlacesResponse getRoutePlaces(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteErrorCode.ROUTE_NOT_FOUND));

        List<RouteStoreDTO> places = storeRepository.findStoresWithCalculatedDistance(routeId);

        // 첫 번째 장소 처리
        if (!places.isEmpty()) {
            RouteStoreDTO firstPlace = places.get(0);
            places.set(0, RouteStoreDTO.builder()
                    .storeId(firstPlace.storeId())
                    .name(firstPlace.name())
                    .location(firstPlace.location())
                    .category(firstPlace.category())
                    .orderIndex(firstPlace.orderIndex())
                    .distance("첫번째장소")
                    .image(firstPlace.image())
                    .build());
        }

        return RouteConverter.convertToRoutePlacesDTO(route, places);
    }
}
