package com.project.nupibe.domain.member.controller;

import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.service.MypageService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
@Tag(name = "마이페이지 API")
public class MypageController {
    private final MypageService mypageService;

    @Operation(method = "GET",summary = "사용자 정보 조회", description = "현재는 id값 가져오면 사용자 정보를 조회합니다.")
    @GetMapping("{memberId}")
    public CustomResponse<MypageResponseDTO.MypageDTO> getMypage(@PathVariable("memberId") Long id){
        MypageResponseDTO.MypageDTO responseDto = mypageService.getMypage(id);
        return CustomResponse.onSuccess(responseDto);
    }

    @Operation(method = "GET",summary = "사용자 저장 장소 조회", description = "memberId값을 가지고 저장한 장소들의 목록을 조회합니다.")
    @GetMapping("{memberId}/stores")
    public CustomResponse<MypageResponseDTO.MypageStoresDTO> getBookmarkStore(@PathVariable("memberId") Long id){
        MypageResponseDTO.MypageStoresDTO responseDto = mypageService.getMemberStore(id);
        return CustomResponse.onSuccess(responseDto);
    }


}
