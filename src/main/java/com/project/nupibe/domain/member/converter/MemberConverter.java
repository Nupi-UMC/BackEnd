package com.project.nupibe.domain.member.converter;

import com.project.nupibe.domain.member.dto.response.MypageResponseDTO;
import com.project.nupibe.domain.member.entity.Member;
import com.project.nupibe.domain.route.entity.Route;
import com.project.nupibe.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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



    public static MypageResponseDTO.MemberRouteDTO toMemberRouteDTO(Route route, String image) {
        return MypageResponseDTO.MemberRouteDTO.builder()
                .routeId(route.getId())
                .name(route.getRouteName())
                .location(route.getLocation())
                .routePic(image)
                .build();
    }

    public static MypageResponseDTO.MypageRoutesDTO toMypageRoutesDTO(List<Route> routes, List<String> images) {
        List<MypageResponseDTO.MemberRouteDTO> routeList = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            MypageResponseDTO.MemberRouteDTO route = toMemberRouteDTO(routes.get(i), images.get(i));
            routeList.add(route);
        }

        return MypageResponseDTO.MypageRoutesDTO.builder()
                .routes(routeList)
                .build();
    }

}
