package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.store.exception.code.StoreErrorCode;
import com.project.nupibe.domain.store.exception.handler.StoreException;
import com.project.nupibe.global.apiPayload.code.GeneralErrorCode;
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
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return MypageResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .build();
    }
}
