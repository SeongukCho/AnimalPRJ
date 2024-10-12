package kopo.poly.service.impl;

import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IWeatherAPIService;
import kopo.poly.service.IWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class WeatherService implements IWeatherService {

    @Value("${api.weatherKey}")
    private String apikey;
    private final IWeatherAPIService weatherAPIService;

    @Override
    public WeatherDTO getWeatherInfo() throws Exception {

        log.info(this.getClass().getName() + ".getWeatherInfo Start!");

        WeatherDTO rDTO = weatherAPIService.getWeatherInfo("1", "7", "JSON", "11B10101", apikey);

        Map<String, Object> body = (Map<String, Object>) rDTO.response().get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

        String announceTime = "";
        String rnSt = "";
        String ta = "";
        String wf = "";

        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0) {
                    Map<String, Object> firstItem = itemList.get(j);
                    Object temp = firstItem.get("announceTime"); // 발표 시간
                    if (temp!=null) {
                        announceTime = temp.toString();
                        break;
                    }
                } else if (i == 1) {
                    Map<String, Object> firstItem = itemList.get(j);
                    Object temp = firstItem.get("rnSt"); // 강수 확률
                    if (temp!=null) {
                        rnSt = temp.toString();
                        break;
                    }
                } else if (i == 2) {
                    Map<String, Object> firstItem = itemList.get(j);
                    String temp = (String) firstItem.get("ta"); // 예상 기온
                    if (!temp.isEmpty()) {
                        ta = temp;
                        break;
                    }
                } else {
                    Map<String, Object> firstItem = itemList.get(j);
                    Object temp = firstItem.get("wf"); // 날씨
                    if (temp!=null) {
                        wf = temp.toString();
                        break;
                    }
                }
            }
        }

        // 날짜 포맷 변환
        String month = announceTime.substring(4, 6);
        String day = announceTime.substring(6, 8);
        announceTime = month + "월" + day + "일";

        WeatherDTO wDTO = WeatherDTO.builder()
                .announceTime(announceTime)
                .rnSt(rnSt)
                .ta(ta)
                .wf(wf)
                .build();

        log.info("announceTime : "+ announceTime + " rnSt : " + rnSt + " ta : "+ ta + " wf : " + wf);

        log.info(this.getClass().getName() + ".getWeatherInfo End!");

        return wDTO;
    }
}