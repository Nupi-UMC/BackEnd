package com.project.nupibe.domain.route.converter;

import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.dto.response.RoutePlacesResDTO;
import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.route.entity.Route;

import java.util.List;
import java.util.stream.Collectors;

public class RouteConverter {

    public static RouteDetailResDTO.RouteDetailResponse convertToRouteDetailDTO(Route route, List<RouteStoreDTO> storeList) {
        List<RouteDetailResDTO.StoreSummary> stores = storeList.stream()
                .map(store -> RouteDetailResDTO.StoreSummary.builder()
                        .storeId(store.storeId())
                        .storeName(store.name())
                        .image(store.image())
                        .build())
                .collect(Collectors.toList());

        return RouteDetailResDTO.RouteDetailResponse.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .content(route.getContent())
                .location(route.getLocation())
                .likeNum(route.getLikeNum())
                .bookmarkNum(route.getBookmarkNum())
                .storeList(stores)
                .build();
    }

    public static RoutePlacesResDTO.RoutePlacesResponse convertToRoutePlacesDTO(Route route, List<RouteStoreDTO> places) {
        List<RoutePlacesResDTO.PlaceDetail> placeDetails = places.stream()
                .map(place -> RoutePlacesResDTO.PlaceDetail.builder()
                        .storeId(place.storeId())
                        .name(place.name())
                        .location(place.location())
                        .category(place.category())
                        .order(place.orderIndex())
                        .distance(place.distance() != null ? place.distance() : "첫번째장소") // distance가 null이면 "첫번째장소"로 처리
                        .build())
                .collect(Collectors.toList());

        return RoutePlacesResDTO.RoutePlacesResponse.builder()
                .routeName(route.getRouteName())
                .date(route.getDate().toLocalDate().toString())
                .places(placeDetails)
                .build();
    }
}
