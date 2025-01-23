package com.project.nupibe.domain.route.dto.response;

import lombok.Builder;

import java.util.List;

public class RouteDetailResDTO {

    @Builder
    public record RouteDetailResponse(
            Long routeId,
            String routeName,
            String content,
            String location,
            int likeNum,
            int bookmarkNum,
            List<StoreSummary> storeList
    ) {
    }

    @Builder
    public record StoreSummary(
            Long storeId,
            String storeName,
            String image
    ) {
    }

    @Builder
    public record StoreImage(
            String image
    ) {
    }

    @Builder
    public record RoutePreviewResponse(
            Long routeId,
            String routeName,
            List<StoreImage> image,
            String location,
            int likeNum,
            int bookmarkNum
    ) {
    }

    @Builder
    public record RoutePageResponse(
            List<RouteDetailResDTO.RoutePreviewResponse> routeList,
            boolean hasNext,
            Long cursor
    ){}
}
