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
}
