package com.project.nupibe.domain.member.controller;

import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.service.MypageService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import com.project.nupibe.global.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
@Tag(name = "마이페이지 API")
public class MypageController {
    private final MypageService mypageService;
    private final SecurityUtil securityUtil;

    @Operation(method = "GET",summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @GetMapping("/")
    public CustomResponse<MypageResponseDTO.MypageDTO> getMypage(@RequestHeader("JWT-TOKEN") String authorizationHeader){
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);
        MypageResponseDTO.MypageDTO responseDto = mypageService.getMypage(memberId);
        return CustomResponse.onSuccess(responseDto);
    }

    @Operation(method = "GET",summary = "사용자 저장 장소 조회", description = "저장한 장소들의 목록을 조회합니다.")
    @GetMapping("/stores")
    public CustomResponse<MypageResponseDTO.MypageStoresDTO> getBookmarkStore(@RequestHeader("JWT-TOKEN") String authorizationHeader){
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);
        MypageResponseDTO.MypageStoresDTO responseDto = mypageService.getMemberStore(memberId);
        return CustomResponse.onSuccess(responseDto);
    }

    @Operation(method = "GET", summary = "사용자 저장 경로 조회", description = "routeType을 받아 저장한 경로들의 목록을 조회합니다.")
    @GetMapping("/routes")
    public CustomResponse<MypageResponseDTO.MypageRoutesDTO> getBookmarkRoute(
            @RequestHeader("JWT-TOKEN") String authorizationHeader,
            @RequestParam(value = "routeType", required = true, defaultValue = "created") String routeType) {
        Long memberId = securityUtil.getMemberIdFromToken(authorizationHeader);
        MypageResponseDTO.MypageRoutesDTO routes = mypageService.getMemberRoute(memberId, routeType);
        return CustomResponse.onSuccess(routes);
    }


}
