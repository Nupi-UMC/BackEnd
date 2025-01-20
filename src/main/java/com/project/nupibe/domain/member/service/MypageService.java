package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.converter.MemberConverter;
import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    //Repository
    private final MemberRepository memberRepository;
    private final MemberStoreRepository memberStoreRepository;

    @Transactional
    public MypageResponseDTO.MypageDTO getMypage(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return MemberConverter.toMypageDTO(member);
    }
    @Transactional
    public MypageResponseDTO.MypageStoresDTO getMemberStore(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        List<Store> stores = memberStoreRepository.findStoresByMemberId(member.getId());

        List<MypageResponseDTO.MemberStoreDTO> storeList = MemberConverter.toMemberStoreDTOList(stores);

        return MypageResponseDTO.MypageStoresDTO.builder()
                .bookmarkedStores(storeList)
                .build();
    }


}
