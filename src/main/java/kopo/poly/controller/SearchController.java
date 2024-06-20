package kopo.poly.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Controller
public class SearchController {

    @Value("${api.serviceKey}")
    private String serviceKey;

    @GetMapping(value = "sido")
    public String sidoController(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".sidoController Start!");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sido"); //URL
        urlBuilder.append("?" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); // 한 페이지 결과 수 (1, 000 이하)
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 번호
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // xml(기본값) 또는 json
        urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8")); // Service Key

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        System.out.println(sb.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        List<SearchDTO> sidoList = new ArrayList<>();
        if (itemsNode.isArray() && !itemsNode.isEmpty()) {
            for (JsonNode itemNode : itemsNode) {
                SearchDTO code = SearchDTO.builder()
                        .orgCd(itemNode.path("orgCd").asText())
                        .orgdownNm(itemNode.path("orgdownNm").asText())
                        .build();

                sidoList.add(code);
            }
        }
        log.info("sidoList : " + sidoList);

        log.info(this.getClass().getName() + ".sidoController End!");

        model.addAttribute("sidoList", sidoList);

        return "test";
    }

    @GetMapping(value = "sigungu")
    public String sigunguController(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".sigunguController Start!");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/sigungu"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("upr_cd","UTF-8") + "=" + URLEncoder.encode("6110000", "UTF-8")); /*시군구 상위코드(시도코드) (입력 시 데이터 O, 미입력 시 데이터 X)*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode(" ", "UTF-8")); /*xml(기본값) 또는 json*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        System.out.println(sb.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        List<SearchDTO> sigunguList = new ArrayList<>();
        if (itemsNode.isArray() && !itemsNode.isEmpty()) {
            for (JsonNode itemNode : itemsNode) {
                SearchDTO code = SearchDTO.builder()
                        .orgCd(itemNode.path("orgCd").asText())
                        .orgdownNm(itemNode.path("orgdownNm").asText())
                        .build();

                sigunguList.add(code);
            }
        }
        log.info("sigunguList : " + sigunguList);

        log.info(this.getClass().getName() + ".sigunguController End!");

        model.addAttribute("sigunguList", sigunguList);

        return "test";
    }
}

