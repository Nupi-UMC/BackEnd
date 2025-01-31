package com.project.nupibe.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class RequestSignupDto {

    @NotNull(message = "Verification ID is required.")
    private Long verificationId;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Nickname is required.")
    private String nickname;

    public RequestSignupDto(Long verificationId, String email, String password, String nickname) {
        this.verificationId = verificationId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}