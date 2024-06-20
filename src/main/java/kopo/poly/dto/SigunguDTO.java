package kopo.poly.dto;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record SigunguDTO(

        /* 요청 */

        /* 응답 */
        String uprCd,           // 시군구 상위 코드
        String orgCd,           // 시도 코드
        String orgdownNm        // 시도 명
) {
}
