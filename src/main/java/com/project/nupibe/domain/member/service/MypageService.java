package com.project.nupibe.domain.member.service;

import com.project.nupibe.domain.member.converter.MemberConverter;
import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.member.repository.MemberRouteRepository;
import com.project.nupibe.domain.member.repository.MemberStoreRepository;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.route.repository.RouteRepository;
import com.project.nupibe.domain.route.repository.RouteStoreRepository;
import com.project.nupibe.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    //Repository
    private final MemberRepository memberRepository;
    private final MemberStoreRepository memberStoreRepository;
    private final RouteRepository routeRepository;
    private final MemberRouteRepository memberRouteRepository;
    private final RouteStoreRepository routeStoreRepository;

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

    @Transactional
    public MypageResponseDTO.MypageRoutesDTO getMemberRoute(Long memberId, String routeType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        List<Route> routes = new ArrayList<>();
        switch (routeType) {
            case "created":
                routes = routeRepository.findByMember(member);
                break;
            case "saved":
                routes = memberRouteRepository.findRoutesByMemberId(member.getId());
                break;
        }

        List<String> images = new ArrayList<>();
        for (Route route : routes) {
            String pic = routeStoreRepository.findFirstImage(route);
            images.add(pic);
        }

        return MemberConverter.toMypageRoutesDTO(routes, images);
    }


}
