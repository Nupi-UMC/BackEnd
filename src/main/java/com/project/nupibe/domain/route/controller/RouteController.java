package com.project.nupibe.domain.route.controller;

import com.project.nupibe.domain.route.dto.response.RouteResDTO;
import com.project.nupibe.domain.route.service.query.RouteQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
@Tag(name = "경로 API", description = "경로 관련 CRUD 및 기능 API")
public class RouteController {

    private final RouteQueryService routeQueryService;

    @GetMapping("/{routeId}")
    @Operation(summary = "경로 상세 조회 API", description = "PathVariable로 보낸 id의 경로를 상세 조회 합니다.")
    public CustomResponse<RouteResDTO.RouteDetailResponse> getRouteDetail(@PathVariable Long routeId) {
        RouteResDTO.RouteDetailResponse response = routeQueryService.getRouteDetail(routeId);
        return CustomResponse.onSuccess(response);
    }
}
