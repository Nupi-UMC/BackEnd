package com.project.nupibe.domain.route.dto.response;

import lombok.Builder;

@Builder
public record RouteStoreDTO(
        Long storeId,
        String name,
        String location,
        String category,
        Integer orderIndex,
        String distance, // 거리 정보 (nullable)
        String image
) {
    public RouteStoreDTO withDistance(String distance) {
        return new RouteStoreDTO(
                this.storeId,
                this.name,
                this.location,
                this.category,
                this.orderIndex,
                distance,
                this.image
        );
    }
}
