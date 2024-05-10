package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record UserInfoDTO(
        String userId, // 회원 아이디
        String userName, // 회원 이름
        String password, // 비밀번호
        String email, // 이메일
        String addr1, // 주소
        String addr2, // 상세 주소
        String regId, // 등록자 아이디
        String regDt, // 등록 일시
        String chgId, // 최근 수정 아이디
        String chgDt, // 최근 수정일시
        String existsYn, // 회원아이디 존재여부
        String roles // 회원 권한
) {
}
