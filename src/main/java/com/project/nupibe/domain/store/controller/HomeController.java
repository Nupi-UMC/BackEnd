package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.service.HomeCommandService;
import com.project.nupibe.domain.store.service.HomeQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeQueryService homeQueryService;
    private final HomeCommandService homeCommandService;

    @GetMapping("/{memberId}")
    public CustomResponse<HomeResponseDTO.GetHomeResponseDTO> getHome(@PathVariable Long memberId) {
        HomeResponseDTO.GetHomeResponseDTO getHomeResponseDTO = homeQueryService.getHome(memberId);
        return CustomResponse.onSuccess(getHomeResponseDTO);
    }

    @GetMapping("/{memberId}/search")
    public CustomResponse<HomeResponseDTO.entertainmentDTO> searchEntertainment
            (@PathVariable Long memberId,
             @RequestParam double latitude,
             @RequestParam double longitude,
             @RequestParam(value = "category", required = false, defaultValue = "0") int category,
             @RequestParam(value = "sort", required = false, defaultValue = "default") String sort) {
        HomeResponseDTO.entertainmentDTO getEntertainmentDTO = homeQueryService.getEntertainment(memberId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(getEntertainmentDTO);
    }

    @GetMapping("/{memberId}/route")
    public CustomResponse<HomeResponseDTO.myRouteDTO> getRoute(
            @PathVariable Long memberId,
            @RequestParam(value = "myRoute", required = true, defaultValue = "created") String routeType) {
        HomeResponseDTO.myRouteDTO routes = homeQueryService.getRoute(memberId, routeType);
        return CustomResponse.onSuccess(routes);
    }

    @GetMapping("/{memberId}/{groupName}")
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getGroupStore(
            @PathVariable Long memberId, @PathVariable String groupName) {
        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getGroupStore(memberId, groupName);
        return CustomResponse.onSuccess(stores);
    }

    @PostMapping("/{memberId}/save/{storeId}")
    public CustomResponse<HomeResponseDTO.savedDTO> saveStore(@PathVariable Long memberId, @PathVariable Long storeId) {
        HomeResponseDTO.savedDTO saved = homeCommandService.saveStore(memberId, storeId);
        return CustomResponse.onSuccess(saved);
    }

    @GetMapping("/{memberId}/{regionId}")
    public CustomResponse<HomeResponseDTO.groupStoreDTO> getRegionStore(
            @PathVariable Long memberId, @PathVariable Long regionId,
            @RequestParam double latitude, @RequestParam double longitude,
            @RequestParam(value = "category", required = false, defaultValue = "0") int category,
            @RequestParam(value = "sort", required = false, defaultValue = "default") String sort ) {
        HomeResponseDTO.groupStoreDTO stores = homeQueryService.getRegionStore(memberId, regionId, latitude, longitude, category, sort);
        return CustomResponse.onSuccess(stores);
    }
}
