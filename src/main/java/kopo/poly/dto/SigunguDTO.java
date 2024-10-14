package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record SigunguDTO(

        /* 요청 */
        String uprCd,           // 시군구 상위 코드
        String type,
        String serviceKey,

        /* 응답 */
        String orgCd,           // 시도 코드
        String orgdownNm        // 시도 명
) {
}
