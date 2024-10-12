package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;


@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record WeatherDTO (

        String pageNo, // 페이지 번호
        String numOfRows, // 한 페이지 결과 수
        String dataType, // 응답 자료 형식
        String regId, // 예보 구역 코드
        String authKey, // 인증키


        /* 여기서부터 리턴 데이터 */
        Map<String, Object> response, // json 리턴 값
        String announceTime, // 발표 시간
        String ta, // 예상 기온 (℃)
        String wf, // 날씨
        String rnSt // 강수 확률

){
}