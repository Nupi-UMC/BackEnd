package com.project.nupibe.domain.route.controller;


import com.project.nupibe.domain.route.dto.RouteCreateRequestDto;
import com.project.nupibe.domain.route.dto.RouteResponseDto;
import com.project.nupibe.domain.route.dto.RouteStoreDto;
import com.project.nupibe.domain.route.dto.response.RouteDetailResDTO;
import com.project.nupibe.domain.route.dto.response.RoutePlacesResDTO;
import com.project.nupibe.domain.route.service.KakaoNaviService;
import com.project.nupibe.domain.route.service.RouteService;
import com.project.nupibe.domain.route.service.RouteCommandService;
import com.project.nupibe.domain.route.service.query.RouteQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
@Tag(name = "경로 API", description = "경로 관련 CRUD 및 기능 API")
public class RouteController {

    private final RouteQueryService routeQueryService;
    private final RouteCommandService routeCommandService;

  //  private final KakaoNaviService kakaoNaviService;
    private final RouteService routeService;


    @GetMapping("/{routeId}")
    @Operation(summary = "경로 상세 조회 API", description = "PathVariable로 보낸 id의 경로를 상세 조회 합니다.")
    public CustomResponse<RouteDetailResDTO.RouteDetailResponse> getRouteDetail(@PathVariable Long routeId) {
        RouteDetailResDTO.RouteDetailResponse response = routeQueryService.getRouteDetail(routeId);
        return CustomResponse.onSuccess(response);
    }

    @GetMapping("/{routeId}/stores")
    @Operation(summary = "경로 내 장소 목록 조회 API", description = "PathVariable로 보낸 id의 경로 내 장소들을 조회 합니다.")
    public CustomResponse<RoutePlacesResDTO.RoutePlacesResponse> getRoutePlaces(@PathVariable Long routeId) {
        RoutePlacesResDTO.RoutePlacesResponse response = routeQueryService.getRoutePlaces(routeId);
        return CustomResponse.onSuccess(response);
    }
  
  @PostMapping
    @Operation(summary = "최적 경로 요청 API", description = "출발지, 도착지 및 경유지를 포함한 최적 경로를 요청합니다.")
    public ResponseEntity<RouteResponseDto> getOptimalRoute(@RequestBody RouteCreateRequestDto requestDto) {
        RouteResponseDto response = routeService.createRoute(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "경로 검색 조회 API", description = "키워드 검색을 통한 경로 조회 API입니다.")
    public CustomResponse<RouteDetailResDTO.RoutePageResponse> getRoutesWithQuery(
            @RequestParam(value="query") String query,
            @RequestParam(value="cursor", defaultValue = "0") int cursor,
            @RequestParam(value = "offset", defaultValue = "8") int offset
    ){
        RouteDetailResDTO.RoutePageResponse result = routeQueryService.getRoutesWithQuery(query, cursor, offset);
        return CustomResponse.onSuccess(result);
    }
  
    @PostMapping("{routeId}/bookmark")
    @Operation(method = "POST", summary = "경로 북마크 API", description = "경로 조회 내 북마크 버튼을 클릭시 작동하는 기능입니다.")
    public CustomResponse<RouteDetailResDTO.savedDTO> bookmarkRoute(@RequestParam Long memberId, @PathVariable("routeId") Long routeId) {
        RouteDetailResDTO.savedDTO saved = routeCommandService.bookmarkRoute(memberId, routeId);
        return CustomResponse.onSuccess(saved);
    }
    @PostMapping("{routeId}/like")
    @Operation(method = "POST", summary = "경로 좋아요 API", description = "경로 조회 내 좋아요 버튼을 클릭시 작동하는 기능입니다.")
    public CustomResponse<RouteDetailResDTO.savedDTO> likeRoute(@RequestParam Long memberId, @PathVariable("routeId") Long routeId) {
        RouteDetailResDTO.savedDTO saved = routeCommandService.likeRoute(memberId, routeId);
        return CustomResponse.onSuccess(saved);
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "경로 검색 조회 API", description = "키워드 검색을 통한 경로 조회 API입니다.")
    @Parameters({
            @Parameter(name = "cursor", description = "커서 값, 처음이면 0"),
            @Parameter(name = "query", description = "쿼리 거리순, 북마크순, 추천순")
    })
    public CustomResponse<RouteDetailResDTO.RoutePageResponse> getRoutesWithQuery(
            @RequestParam (value="query", defaultValue = "거리순") String query,
            @RequestParam (value="search") String search,
            @RequestParam(value="cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "offset", defaultValue = "8") int offset,
            @RequestParam(value="latitude",required = false) Float lat,
            @RequestParam(value="longitude",required = false) Float lng
    ){
        float latitude = (lat != null) ? lat : 0.0f;
        float longitude = (lng != null) ? lng : 0.0f;
        RouteDetailResDTO.RoutePageResponse result = routeQueryService.getRoutesWithQuery(query, latitude, longitude, search, cursor, offset);
        return CustomResponse.onSuccess(result);
    }
}

