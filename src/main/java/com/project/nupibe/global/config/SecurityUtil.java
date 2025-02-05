package com.project.nupibe.global.config;
;
import com.project.nupibe.domain.member.exception.code.MemberErrorCode;
import com.project.nupibe.domain.member.exception.code.TokenErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;

    public Long getMemberIdFromTokens() {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new MemberException(TokenErrorCode.REFRESH_TOKEN_MISSING);
        }

        String token = authorizationHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new MemberException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long memberId = jwtTokenProvider.extractMemberId(token);
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.NOT_FOUND);
        }

        return memberId;
    }
}
