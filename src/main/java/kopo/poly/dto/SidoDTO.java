package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record SidoDTO(

        // 요청
        int numOfRows,
        int pageNo,
        String type,
        String serviceKey,

        // 응답

        String orgCd,
        String orgdownNm,

        Map<String, Object> message
        ) {
}
