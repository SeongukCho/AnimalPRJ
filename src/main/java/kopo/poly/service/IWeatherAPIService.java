package kopo.poly.service;

import feign.Param;
import feign.RequestLine;
import kopo.poly.dto.WeatherDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="WeatherAPI", url="https://apihub.kma.go.kr")
public interface IWeatherAPIService {

    @RequestLine("GET /api/typ02/openApi/VilageFcstMsgService/getLandFcst?pageNo={pageNo}&numOfRows={numOfRows}&dataType={dataType}&regId={regId}&authKey={authKey}")
    WeatherDTO getWeatherInfo(
            @Param("pageNo") String pageNo,
            @Param("numOfRows") String numOfRows,
            @Param("dataType") String dataType,
            @Param("regId") String regId,
            @Param("authKey") String authKey
    );
}