package kopo.poly.dto;

import lombok.Builder;

@Builder
public record TokenDTO(

        String access_token,               // 사용자 액세스 토큰 값
        String refresh_token,              // 사용자 리프레시 토큰 값
        Integer refresh_token_expires_in,  // 리프레시 토큰 만료 시간(초)
        Integer expires_in,                // 액세스 토큰과 ID 토큰의 만료시간(초)(액세스 토큰과 ID 토큰의 만료 시간 동일
        String scope,                      // 인증된 사용자의 정보 조회 권한 범위(범위가 여러 개일 경우, 공백 구분)
        String token_type                  // 토큰 타입, bearer 고정

) {
}