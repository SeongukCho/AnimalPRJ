package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ShelterDTO(

        /* 요청 */
        String uprCd,
        String orgCd,
        String type,

        /* 응답 */
        String careRegNo,         // 보호소번호
        String careNm           // 보호소명

) {
}
