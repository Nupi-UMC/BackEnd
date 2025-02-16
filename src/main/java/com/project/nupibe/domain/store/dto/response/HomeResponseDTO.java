package com.project.nupibe.domain.store.dto.response;

import lombok.Builder;
import java.util.List;

public class HomeResponseDTO {
    @Builder
    public record GetHomeResponseDTO(
            UpcommingScheduleDTO Upcomming,
            List<groupNameDTO> groupList,
            List<regionDTO> regions,
            List<SpotDescription> steadySpots
    ) {}

    @Builder
    public record UpcommingScheduleDTO(
            String title,
            String date
    ) {}

    @Builder
    public record groupNameDTO(
            String groupName
    ) {}

    @Builder
    public record regionDTO(
            int regionId,
            String regionName
    ){};

    @Builder
    public record entertainmentDTO(
            categoryDTO category,
            String sort,
            List<storeDTO> stores
    ) {};

    @Builder
    public record groupStoreDTO(
            List<storeDTO> stores
    ) {};

    @Builder
    public record categoryDTO(
            int cateogoryId,
            String category
    ) {};

    @Builder
    public record storeDTO(
            Long storeId,
            String storeName,
            String storePic,
            String storePlace,
            boolean saved
    ) {};

    @Builder
    public record myRouteDTO(
            List<routesDTO> routes
    ) {};

    @Builder
    public record routesDTO(
            Long routeId,
            String routeName,
            String routeLocation,
            String routePic
    ){};

    @Builder
    public record savedDTO(
            Long storeId,
            boolean saved
    ) {};

    @Builder
    public record SpotDescription(
            String name,
            String place,
            String location,
            String description
    ) {}
}
