package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.service.HomeCommandService;
import com.project.nupibe.domain.store.service.HomeQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import com.project.nupibe.global.config.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeQueryService homeQueryService;
    private final HomeCommandService homeCommandService;
    private final SecurityUtil securityUtil;

    @GetMapping("")
    public CustomResponse<HomeResponseDTO.GetHomeResponseDTO> getHome(@RequestHeader("JWT-TOKEN") String authorizationHeader) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);

        HomeResponseDTO.GetHomeResponseDTO getHomeResponseDTO = homeQueryService.getHome(memberId);
        return CustomResponse.onSuccess(getHomeResponseDTO);
    }

    @GetMapping("/search")
    public CustomResponse<HomeResponseDTO.entertainmentDTO> searchEntertainment
            (@RequestHeader("JWT-TOKEN") String authorizationHeader,
             @RequestParam double latitude,
             @RequestParam double longitude,
             @RequestParam(value = "category", required = false, defaultValue = "0") int category,
             @RequestParam(value = "sort", required = false, defaultValue = "default") String sort) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);

        HomeResponseDTO.entertainmentDTO getEntertainmentDTO = homeQueryService.getEntertainment(memberId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(getEntertainmentDTO);
    }

    @GetMapping("/route")
    public CustomResponse<HomeResponseDTO.myRouteDTO> getRoute(
            @RequestHeader("JWT-TOKEN") String authorizationHeader,
            @RequestParam(value = "myRoute", required = true, defaultValue = "created") String routeType) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);

        HomeResponseDTO.myRouteDTO routes = homeQueryService.getRoute(memberId, routeType);
        return CustomResponse.onSuccess(routes);
    }

    @GetMapping("/{groupName}")
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getGroupStore(
            @RequestHeader("JWT-TOKEN") String authorizationHeader, @PathVariable String groupName) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);

        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getGroupStore(memberId, groupName);
        return CustomResponse.onSuccess(stores);
    }

    @PostMapping("/save/{storeId}")
    public CustomResponse<HomeResponseDTO.savedDTO> saveStore(@RequestHeader("JWT-TOKEN") String authorizationHeader, @PathVariable Long storeId) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);

        HomeResponseDTO.savedDTO saved = homeCommandService.saveStore(memberId, storeId);
        return CustomResponse.onSuccess(saved);
    }

    @GetMapping("/{regionId}")
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getRegionStore(
            @RequestHeader("JWT-TOKEN") String authorizationHeader, @PathVariable Long regionId,
            @RequestParam double latitude, @RequestParam double longitude,
            @RequestParam(value = "category", required = false, defaultValue = "0") int category,
            @RequestParam(value = "sort", required = false, defaultValue = "default") String sort ) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);
        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getRegionStore(memberId, regionId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(stores);
    }
}
