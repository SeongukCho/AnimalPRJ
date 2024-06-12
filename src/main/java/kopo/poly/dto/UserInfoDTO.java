package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record UserInfoDTO(
        String userId, // 회원 아이디
        String userName, // 회원 이름
        String nickName, // 회원 닉네임
        String password, // 비밀번호
        String email, // 이메일
        String regDt, // 등록 일시
//        String petYn, // 반려동물 유무

        int authNumber,
        String existsYn // 회원아이디 존재여부

) {
}
