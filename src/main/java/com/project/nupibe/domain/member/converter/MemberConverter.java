package com.project.nupibe.domain.member.converter;

import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {

    // Member Entity -> MypageDTO
    public static MypageResponseDTO.MypageDTO toMypageDTO(Member member) {
        return MypageResponseDTO.MypageDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .build();
    }

    // Store Entity -> MemberStoreDTO
    public static MypageResponseDTO.MemberStoreDTO toMemberStoreDTO(Store store) {
        return MypageResponseDTO.MemberStoreDTO.builder()
                .name(store.getName())
                .location(store.getLocation())
                .build();
    }

    // List<Store> -> List<MemberStoreDTO>
    public static List<MypageResponseDTO.MemberStoreDTO> toMemberStoreDTOList(List<Store> stores) {
        return stores.stream()
                .map(MemberConverter::toMemberStoreDTO) // Store를 DTO로 변환
                .collect(Collectors.toList());
    }



    // Route Entity -> MemberRouteDTO
    public static MypageResponseDTO.MemberRouteDTO toMemberRouteDTO(Route route) {
        return MypageResponseDTO.MemberRouteDTO.builder()
                .name(route.getRouteName())
                .location(route.getLocation())
                .build();
    }

    // List<Route> -> List<MemberRouteDTO>
    public static List<MypageResponseDTO.MemberRouteDTO> toMemberRouteDTOList(List<Route> routes) {
        return routes.stream()
                .map(MemberConverter::toMemberRouteDTO) // Route를 DTO로 변환
                .collect(Collectors.toList());
    }

}
