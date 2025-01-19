package com.project.nupibe.domain.member.dto.response;

import lombok.Builder;

import java.util.List;

public class MypageResponseDTO{
    @Builder
    public record MypageDTO (
        String email,
        String nickname,
        String profile
    ){}

    @Builder
    public record MemberStoreDTO (
            String name,
            String location
    ){}

    @Builder
    public record MypageStoresDTO (
            List<MemberStoreDTO> bookmarkedStores
    ){}



}
