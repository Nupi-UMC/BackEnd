package com.project.nupibe.domain.route.dto.response;

import lombok.Builder;

import java.util.List;

public class RoutePlacesResDTO {

    @Builder
    public record RoutePlacesResponse(
            String routeName,
            String date,
            List<PlaceDetail> places
    ) {
    }

    @Builder
    public record PlaceDetail(
            Long storeId,
            String name,
            String location,
            String category,
            int order,
            String distance
    ) {
    }
}
