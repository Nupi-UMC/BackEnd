package com.project.nupibe.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestTokenDto(
        @NotNull(message = "Refresh Token이 누락되었습니다.")
        String refreshToken
) {}