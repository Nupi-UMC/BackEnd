package com.project.nupibe.domain.controller;

import com.project.nupibe.domain.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @Operation(summary = "사용자 정보 조회", description = "현재는 id값 가져오면")
    @GetMapping("")
    public ResponseEntity<?> getMypage(){
        //사용자의 ID가져오기
        Long id = 1L;
        MypageResponseDTO responseDto = mypageService.getMypage(id);
        return ResponseEntity.ok(responseDto);
    }
}
