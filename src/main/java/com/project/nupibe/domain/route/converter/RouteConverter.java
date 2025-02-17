package com.project.nupibe.domain.route.converter;

import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.dto.response.RoutePlacesResDTO;
import com.project.nupibe.domain.route.dto.response.RouteStoreDTO;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.entity.RouteStore;
import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class RouteConverter {

    public static RouteDetailResDTO.RouteDetailResponse convertToRouteDetailDTO(
            Route route, boolean isLiked, boolean isBookmarked, List<RouteStoreDTO> storeList) {
        List<RouteDetailResDTO.StoreSummary> stores = storeList.stream()
                .map(store -> RouteDetailResDTO.StoreSummary.builder()
                        .storeId(store.storeId())
                        .storeName(store.name())
                        .image(store.image())
                        .category(store.category())
                        .distance(store.distance())
                        .latitude(store.latitude())
                        .longitude(store.longitude())
                        .build())
                .collect(Collectors.toList());

        return RouteDetailResDTO.RouteDetailResponse.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .nickName(route.getMember().getNickname())
                .content(route.getContent())
                .location(route.getLocation())
                .likeNum(route.getLikeNum())
                .bookmarkNum(route.getBookmarkNum())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
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

    public static RouteDetailResDTO.RoutePreviewResponse convertToRoutePreviewDTO(Route route, List<RouteStoreDTO> storeList) {
        List<RouteDetailResDTO.StoreImage> stores = storeList.stream()
                .map(store -> RouteDetailResDTO.StoreImage.builder()
                        .image(store.image())
                        .build())
                .collect(Collectors.toList());

        return RouteDetailResDTO.RoutePreviewResponse.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .category(route.getCategory())
                .image(stores)
                .location(route.getLocation())
                .likeNum(route.getLikeNum())
                .bookmarkNum(route.getBookmarkNum())
                .build();
    }

    public static List<RouteStoreDTO> convertRouteStoresToDTOs(List<RouteStore> routeStores) {
        return routeStores.stream()
                .map(routeStore -> RouteStoreDTO.builder()
                        .storeId(routeStore.getStore().getId())
                        .name(routeStore.getStore().getName())
                        .image(routeStore.getStore().getImage())
                        .orderIndex(routeStore.getOrderIndex())
                        .build())
                .collect(Collectors.toList());
    }


    public static List<RouteDetailResDTO.RoutePreviewResponse> toRoutePreviewList(List<Route> routes){
        return routes.stream()
                .map(route -> {
                    List<RouteStoreDTO> storeList = convertRouteStoresToDTOs(route.getRouteStores());
                    return convertToRoutePreviewDTO(route, storeList);
                })
                .collect(Collectors.toList());
    }

    public static RouteDetailResDTO.RoutePageResponse convertToRoutePageDTO(Slice<Route> routes){
        List<RouteDetailResDTO.RoutePreviewResponse> routeList = toRoutePreviewList(routes.getContent());
        return RouteDetailResDTO.RoutePageResponse.builder()
                .routeList(routeList)
                .hasNext(routes.hasNext())
                .cursor(routes.getContent().get(routes.getContent().size() - 1).getId())
                .build();
    }

    public static RouteDetailResDTO.savedDTO save(Long routeId, boolean save) {
        return RouteDetailResDTO.savedDTO.builder()
                .routeId(routeId)
                .saved(save).build();
    }
}
