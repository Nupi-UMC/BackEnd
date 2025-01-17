package com.project.nupibe.domain.route.converter;

import com.project.nupibe.domain.route.dto.response.RouteResDTO;
import com.project.nupibe.domain.route.entity.Route;

import java.util.List;
import java.util.stream.Collectors;

public class RouteConverter {

    public static RouteResDTO.RouteDetailResponse convertToDto(Route route, List<Object[]> stores) {
        List<RouteResDTO.StoreSummary> storeSummaries = stores.stream()
                .map(store -> RouteResDTO.StoreSummary.builder()
                        .storeId((Long) store[0])
                        .storeName((String) store[1])
                        .image((String) store[2])
                        .build()
                )
                .collect(Collectors.toList());

        return RouteResDTO.RouteDetailResponse.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .content(route.getContent())
                .location(route.getLocation())
                .likeNum(route.getLikeNum())
                .bookmarkNum(route.getBookmarkNum())
                .storeList(storeSummaries)
                .build();
    }
}
