package com.project.nupibe.domain.store.service;

import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.repository.MemberRepository;
import com.project.nupibe.domain.region.entity.Region;
import com.project.nupibe.domain.region.repository.RegionRepository;
import com.project.nupibe.domain.store.converter.HomeConverter;
import com.project.nupibe.domain.store.dto.response.HomeResponseDTO;
import com.project.nupibe.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeQueryService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;

    public HomeResponseDTO.GetHomeResponseDTO getHome(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        List<String> groupNames = storeRepository.findAllGroup();
        List<HomeResponseDTO.groupNameDTO> groupList = HomeConverter.toGroupName(groupNames);

        List<HomeResponseDTO.regionDTO> regions = new ArrayList<>();
        for(Region region : regionRepository.findAll()) {
            int id = region.getId();
            String name = region.getName();
            HomeResponseDTO.regionDTO place = HomeConverter.toRegionDTO(id, name);
            regions.add(place);
        }

        return HomeConverter.toGetHome(groupList, regions);
    }
}
