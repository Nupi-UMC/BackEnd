package com.project.nupibe.domain.member.dto.response;

import lombok.Builder;
@Builder
public record MypageResponseDTO (
        String email,
        String nickname,
        String profile
){
}
