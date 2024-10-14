package kopo.poly.dto;

import lombok.Builder;

@Builder
public record KindDTO(

        // 요청
        String upKindCd,
        String type,

        // 응답
        String kindCd,
        String knm
) {
}
