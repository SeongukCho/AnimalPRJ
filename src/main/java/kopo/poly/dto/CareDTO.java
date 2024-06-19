package kopo.poly.dto;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
public record CareDTO(

        /* 요청 */

        /* 응답 */
        Long careRegNo,         // 보호소번호
        String careNm           // 보호소명

) {
}
