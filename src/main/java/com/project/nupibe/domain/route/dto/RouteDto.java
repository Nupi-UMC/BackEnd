package com.project.nupibe.domain.route.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class RouteDto {

    private Long routeId;
    private Long memberId;
    private String routeName;
    private String category;
    private String location;
    private LocalDateTime createdAt;





}
