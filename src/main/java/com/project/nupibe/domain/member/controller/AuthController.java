package com.project.nupibe.domain.member.controller;

import com.project.nupibe.domain.member.dto.request.*;
import com.project.nupibe.domain.member.dto.response.*;
import com.project.nupibe.domain.member.exception.code.SignupErrorCode;
import com.project.nupibe.domain.member.exception.code.TokenErrorCode;
import com.project.nupibe.domain.member.exception.code.VerificationErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.service.*;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "로그인/회원가입 API")
public class AuthController {

    private final VerificationService verificationService;
    private final VerifyService verifyService;
    private final SignupService signupService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final TokenService tokenService;

    public AuthController(VerificationService verificationService, VerifyService verifyService, SignupService signupService, LoginService loginService, LogoutService logoutService, TokenService tokenService) {
        this.verificationService = verificationService;
        this.verifyService = verifyService;
        this.signupService = signupService;
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.tokenService = tokenService;
    }

    @PostMapping("/requestVerification")
    @Operation(summary = "이메일 인증 요청 API", description = "이메일 인증 요청을 위한 API입니다.")
    public CustomResponse<ResponseVerificationDto.Result> requestVerification(
            @Valid @RequestBody RequestVerificationDto requestDto) {
        try {
            String expiresAt = verificationService.requestVerification(requestDto.email());
            return CustomResponse.onSuccess(new ResponseVerificationDto.Result(expiresAt));
        } catch (IllegalArgumentException e) {
            throw new MemberException(VerificationErrorCode.INVALID_EMAIL);
        } catch (Exception e) {
            throw new MemberException(VerificationErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @PostMapping("/verifyCode")
    @Operation(summary = "인증 코드 확인 API", description = "인증 코드 확인을 위한 API입니다.")
    public CustomResponse<ResponseVerifyDto.Result> verifyCode(
            @Valid @RequestBody RequestVerifyDto requestDto) {
        Long verificationId = verifyService.verifyCode(requestDto);
        return CustomResponse.onSuccess(new ResponseVerifyDto.Result(verificationId));
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입을 위한 API입니다.")
    public CustomResponse<ResponseSignupDto.Result> signup(@Valid @RequestBody RequestSignupDto signupDto) {
        try {
            Long userId = signupService.signup(signupDto);
            return CustomResponse.onSuccess(new ResponseSignupDto.Result(userId));
        } catch (IllegalArgumentException e) {
            throw new MemberException(SignupErrorCode.REQUIRED_FIELD_MISSING);
        } catch (MemberException e) {
            throw e;
        } catch (Exception e) {
            throw new MemberException(SignupErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "로그인 요청을 처리하고 JWT 토큰을 반환합니다.")
    public CustomResponse<ResponseLoginDto> login(@Valid @RequestBody RequestLoginDto loginRequestDto) {
        try {
            ResponseLoginDto responseDto = loginService.login(loginRequestDto);
            return CustomResponse.onSuccess(responseDto);
        } catch (IllegalArgumentException e) {
            throw new MemberException(SignupErrorCode.REQUIRED_FIELD_MISSING);
        } catch (MemberException e) {
            throw e;
        } catch (Exception e) {
            throw new MemberException(SignupErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "Access Token을 블랙리스트에 추가하고 Refresh Token을 삭제합니다.")
    public CustomResponse<Void> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return CustomResponse.onFailure("ERROR401", "Authorization header is missing or invalid.");
        }

        String accessToken = authorizationHeader.substring(7);
        logoutService.logout(accessToken);
        return CustomResponse.onSuccess(null);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Access/Refresh Token 재발급", description = "Refresh Token을 사용해 새로운 Access Token과 Refresh Token을 발급합니다.")
    public CustomResponse<ResponseTokenDto> refreshToken(@RequestBody @Valid RequestTokenDto requestTokenDto) {
        if (requestTokenDto.refreshToken() == null || requestTokenDto.refreshToken().isEmpty()) {
            throw new MemberException(TokenErrorCode.REFRESH_TOKEN_MISSING);
        }

        try {
            ResponseTokenDto response = tokenService.refreshToken(requestTokenDto.refreshToken());
            return CustomResponse.onSuccess(response);
        } catch (MemberException e) {
            throw e;
        } catch (Exception e) {
            throw new MemberException(TokenErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}