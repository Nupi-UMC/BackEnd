package com.project.nupibe.domain.service;

import com.project.nupibe.domain.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.repository.MemberRepository;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
import com.project.nupibe.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageService {
    //Repository
    private final MemberRepository memberRepository;

    @Transactional
    public MypageResponseDTO getMypage(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberHandler(GeneralErrorCode.MEMBER_NOT_FOUND));
        return MypageResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .build();
    }
}
