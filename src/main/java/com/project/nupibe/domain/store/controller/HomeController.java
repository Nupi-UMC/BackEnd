package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.service.HomeCommandService;
import com.project.nupibe.domain.store.service.HomeQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import com.project.nupibe.global.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "홈 API")
public class HomeController {
    private final HomeQueryService homeQueryService;
    private final HomeCommandService homeCommandService;
    private final SecurityUtil securityUtil;

    @GetMapping("")
    @Operation(method = "GET", summary = "홈 화면 조회 API", description = "홈화면을 조회하는 API입니다.")
    public CustomResponse<HomeResponseDTO.GetHomeResponseDTO> getHome() {
        //Long memberId = securityUtil.getMemberIdFromTokens();

        HomeResponseDTO.GetHomeResponseDTO getHomeResponseDTO = homeQueryService.getHome(16L);
        return CustomResponse.onSuccess(getHomeResponseDTO);
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "놀거리 탐색 API", description = "현재 위치 주변에 있는 장소들을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "latitude", description = "현재 위치의 위도 값"),
            @Parameter(name = "longitue", description = "현재 위치의 경도 값"),
            @Parameter(name = "category", description = "지정할 카테고리 값"),
            @Parameter(name = "sort", description = "정렬 순서, default: 거리순 / bookamrk: 북마크순 / recommend: 추천순")
    })
    public CustomResponse<HomeResponseDTO.entertainmentDTO> searchEntertainment
            (@RequestParam double latitude,
             @RequestParam double longitude,
             @RequestParam(value = "category", required = false, defaultValue = "0") int category,
             @RequestParam(value = "sort", required = false, defaultValue = "default") String sort) {
        Long memberId = securityUtil.getMemberIdFromTokens();

        HomeResponseDTO.entertainmentDTO getEntertainmentDTO = homeQueryService.getEntertainment(memberId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(getEntertainmentDTO);
    }

    @GetMapping("/route")
    @Operation(method = "GET", summary = "경로 조회 API", description = "생성 또는 저장한 경로를 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "myRoute", description = "생성됨/저장됨 선택, created: 생성된경로/saved: 저장한경로")
    })
    public CustomResponse<HomeResponseDTO.myRouteDTO> getRoute(
            @RequestParam(value = "myRoute", required = true, defaultValue = "created") String routeType) {
        Long memberId = securityUtil.getMemberIdFromTokens();

        HomeResponseDTO.myRouteDTO routes = homeQueryService.getRoute(memberId, routeType);
        return CustomResponse.onSuccess(routes);
    }

    @GetMapping("/group/{groupName}")
    @Operation(method = "GET", summary = "카테고리별 조회 API", description = "지정한 카테고리의 지점들을 조회하는 API입니다.")
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getGroupStore(
            @PathVariable String groupName) {
        Long memberId = securityUtil.getMemberIdFromTokens();

        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getGroupStore(memberId, groupName);
        return CustomResponse.onSuccess(stores);
    }

    @PostMapping("/save/{storeId}")
    @Operation(method = "POST", summary = "놀거리 저장 API", description = "지정한 장소를 저장하는 API입니다.")
    public CustomResponse<HomeResponseDTO.savedDTO> saveStore(@PathVariable Long storeId) {
        Long memberId = securityUtil.getMemberIdFromTokens();

        HomeResponseDTO.savedDTO saved = homeCommandService.saveStore(memberId, storeId);
        return CustomResponse.onSuccess(saved);
    }

    @GetMapping("/{regionId}")
    @Operation(method = "GET", summary = "지역별 놀거리 탐색 API", description = "현재 위치 주변에 있는 장소들을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "latitude", description = "현재 위치의 위도 값"),
            @Parameter(name = "longitue", description = "현재 위치의 경도 값"),
            @Parameter(name = "category", description = "지정할 카테고리 값"),
            @Parameter(name = "sort", description = "정렬 순서, default: 거리순 / bookamrk: 북마크순 / recommend: 추천순")
    })
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getRegionStore(
            @PathVariable Long regionId,
            @RequestParam double latitude, @RequestParam double longitude,
            @RequestParam(value = "category", required = false, defaultValue = "0") int category,
            @RequestParam(value = "sort", required = false, defaultValue = "default") String sort ) {
        Long memberId = securityUtil.getMemberIdFromTokens();
        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getRegionStore(memberId, regionId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(stores);
    }
}
