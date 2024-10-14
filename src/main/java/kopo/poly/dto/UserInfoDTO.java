package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record UserInfoDTO(
        @NotBlank(message = "아이디는 필수 입력 사항입니다.")
        @Size(min = 4, max = 16, message = "아이디는 최소 4글자에서 16글자까지 입력가능합니다.")
        String userId, // 회원 아이디

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Size(max = 10, message = "이름은 10글자까지 입력가능합니다.")
        String userName, // 회원 이름

        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        @Size(max = 10, message = "닉네임은 10글자까지 입력가능합니다.")
        String nickName, // 회원 닉네임

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        @Size(max = 16, message = "비밀번호는 16글자까지 입력가능합니다.")
        String password, // 비밀번호

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Size(max = 30, message = "이메일은 30글자까지 입력가능합니다.")
        @Email String email, // 이메일

        String regDt, // 등록 일시

        int authNumber,
        String existsYn, // 회원아이디 존재여부
        String profilePath // 프로필 사진 경로

) {
}
