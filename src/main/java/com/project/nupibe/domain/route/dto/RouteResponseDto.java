package com.project.nupibe.domain.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RouteResponseDto {

    private Long routeId;
    private String routeName;
    private String date;
    private List<OptimalRoute> optimizedPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Getter
    @AllArgsConstructor
    public static class OptimalRoute {
        private Long storeId;
        private String name;
        private double x;
        private double y;

    }
}
