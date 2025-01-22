package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.service.HomeQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeQueryService homeQueryService;

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
        HomeResponseDTO.myRouteDTO myRoute = homeQueryService.getRoute(memberId, routeType);
        return CustomResponse.onSuccess(myRoute);
    }
}
