package com.project.nupibe.domain.member.controller;

import com.project.nupibe.domain.member.dto.request.RequestSignupDto;
import com.project.nupibe.domain.member.dto.response.ResponseSignupDto;
import com.project.nupibe.domain.member.exception.code.SignupErrorCode;
import com.project.nupibe.domain.member.exception.handler.MemberException;
import com.project.nupibe.domain.member.service.SignupService;
import com.project.nupibe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/signup")
@Tag(name = "회원가입 API")
public class SignupController {

    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "회원가입 API", description = "회원가입을 위한 API입니다.")
    public CustomResponse<ResponseSignupDto.Result> signup(
            @RequestParam("verificationId") Long verificationId,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        try {
            RequestSignupDto signupDto = new RequestSignupDto(verificationId, email, password, nickname);
            signupService.signup(signupDto, profileImage);
            return CustomResponse.onSuccess(new ResponseSignupDto.Result());
        } catch (IllegalArgumentException e) {
            throw new MemberException(SignupErrorCode.REQUIRED_FIELD_MISSING);
        } catch (MemberException e) {
            throw e;
        } catch (Exception e) {
            throw new MemberException(SignupErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}