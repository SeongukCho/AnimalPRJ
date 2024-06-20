package kopo.poly.dto;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record SearchDTO(

        /* 요청 */

        /* 응답 */
        String orgCd,           // 시도 코드
        String orgdownNm        // 시도 명
) {
}
