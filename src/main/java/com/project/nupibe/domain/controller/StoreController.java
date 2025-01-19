package com.project.nupibe.domain.controller;

import com.project.nupibe.domain.converter.StoreConverter;
import com.project.nupibe.domain.dto.response.RouteResponseDTO;
import com.project.nupibe.domain.dto.response.StoreDetailResponseDTO;
import com.project.nupibe.domain.repository.StoreRepository;
import com.project.nupibe.domain.service.StoreService;
import com.project.nupibe.domain.store.entity.Store;
import com.project.nupibe.global.apiPayload.CustomResponse;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import com.project.nupibe.global.apiPayload.exception.handler.StoreHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("api/stores")
@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("{storeId}/detail")
    @Operation(method = "GET", summary = "가게 단일 조회(detail) API", description = "가게 상세페이지 조회입니다.")
    public CustomResponse<StoreDetailResponseDTO> getDetailStore(@PathVariable Long storeId){
        StoreDetailResponseDTO responseDTO = storeService.getStoreDetail(storeId);
        return CustomResponse.onSuccess(responseDTO);
    }


    @PostMapping("{storeId}/bookmark")
    @Operation(method = "POST", summary = "가게 북마크 API", description = "가게 북마크 버튼을 클릭시 작동하는 기능입니다..")
    public ResponseEntity<String> bookmarkStore(@RequestParam Long memberId, @PathVariable Long storeId) {
        storeService.bookmarkStore(memberId, storeId);
        return ResponseEntity.ok("Store bookmarked successfully");
    }

    //@GetMapping("{storeId}/routes")
    //public CustomResponse<RouteResponseDTO> getRoutesByStoreId(@PathVariable Long storeId){
    //}
}
