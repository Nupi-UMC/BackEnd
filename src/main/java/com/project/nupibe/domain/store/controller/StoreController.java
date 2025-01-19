package com.project.nupibe.domain.store.controller;

import com.project.nupibe.domain.store.dto.response.StoreResponseDTO;
import com.project.nupibe.domain.store.service.StoreQueryService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Tag(name = "가게 API")
public class StoreController {
    private final StoreQueryService storeQueryService;

    @GetMapping("/{storeId}/preview")
    @Operation(method = "GET", summary = "가게 단일 조회(preview) API", description = "지도 마커 클릭 시 나오는 가게 단일 조회입니다.")
    public CustomResponse<StoreResponseDTO.StorePreviewDTO> getStorePreview(
            @PathVariable("storeId") Long storeId
    ){
        StoreResponseDTO.StorePreviewDTO result = storeQueryService.getStorePreview(storeId);
        return CustomResponse.onSuccess(HttpStatus.OK, result);
    }

    @GetMapping("/around")
    @Operation(method = "GET", summary = "내 주변 가게 조회 API", description = "현재 위치 주변에 있느 가게들을 조회하는 API입니다.")
    public CustomResponse<StoreResponseDTO.StorePageDTO> getStores(
            @RequestParam(value="cursor", defaultValue = "0") int cursor,
            @RequestParam(value = "offset", defaultValue = "8") int offset,
            @RequestParam(value="latitude", required = true) float lat,
            @RequestParam(value="longitude", required = true) float lng
            ){
        StoreResponseDTO.StorePageDTO result = storeQueryService.getStores(lat, lng, cursor, offset);
        return CustomResponse.onSuccess(HttpStatus.OK, result);
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "가게 검색 조회 API", description = "키워드 검색을 통한 가게 조회 API입니다.")
    public CustomResponse<StoreResponseDTO.StorePageDTO> getStoresWithQuery(
            @RequestParam (value="query") String query,
            @RequestParam(value="cursor", defaultValue = "0") int cursor,
            @RequestParam(value = "offset", defaultValue = "8") int offset
    ){
        StoreResponseDTO.StorePageDTO result = storeQueryService.getStoresWithQuery(query, cursor, offset);
        return CustomResponse.onSuccess(HttpStatus.OK, result);
    }
}
