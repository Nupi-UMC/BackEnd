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
}
