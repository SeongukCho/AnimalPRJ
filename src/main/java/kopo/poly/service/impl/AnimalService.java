package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.BoardDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.repository.entity.BoardEntity;
import kopo.poly.service.IAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimalService implements IAnimalService {

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final ObjectMapper objectMapper;

    @Override
    public List<AnimalDTO> getAnimalList(AnimalDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalList Start!");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(pDTO.bgnde() != null ? pDTO.bgnde() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(pDTO.endde() != null ? pDTO.endde() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(pDTO.upkind() != null ? pDTO.upkind() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode(pDTO.kind() != null ? pDTO.kind() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(pDTO.uprCd() != null ? pDTO.uprCd() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(pDTO.orgCd() != null ? pDTO.orgCd() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode(pDTO.careRegNo() != null ? pDTO.careRegNo() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(pDTO.state() != null ? pDTO.state() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode(pDTO.neuterYnR() != null ? pDTO.neuterYnR() : "", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

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

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());
        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

        List<AnimalDTO> animalList = new ArrayList<>();
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                AnimalDTO animal = AnimalDTO.builder()
                        .desertionNo(itemNode.path("desertionNo").asLong())
                        .fileName(itemNode.path("fileName").asText())
                        .happenDt(itemNode.path("happenDt").asLong())
                        .happenPlace(itemNode.path("happenPlace").asText())
                        .kindCd(itemNode.path("kindCd").asText())
                        .colorCd(itemNode.path("colorCd").asText())
                        .age(itemNode.path("age").asText())
                        .weight(itemNode.path("weight").asText())
                        .noticeNo(itemNode.path("noticeNo").asText())
                        .noticeSdt(itemNode.path("noticeSdt").asLong())
                        .noticeEdt(itemNode.path("noticeEdt").asLong())
                        .popfile(itemNode.path("popfile").asText())
                        .processState(itemNode.path("processState").asText())
                        .sexCd(itemNode.path("sexCd").asText())
                        .neuterYn(itemNode.path("neuterYn").asText())
                        .specialMark(itemNode.path("specialMark").asText())
                        .careNm(itemNode.path("careNm").asText())
                        .careTel(itemNode.path("careTel").asText())
                        .careAddr(itemNode.path("careAddr").asText())
                        .orgNm(itemNode.path("orgNm").asText())
                        .chargeNm(itemNode.path("chargeNm").asText())
                        .officetel(itemNode.path("officetel").asLong())
                        .noticeComment(itemNode.path("noticeComment").asText())
                        // 필요한 다른 필드들도 설정
                        .build();
                // 필요한 다른 필드들도 설정

                animalList.add(animal);
            }
        }
        log.info("animalList : " + animalList);

        return animalList;
    }

    @Override
    public List<ShelterDTO> getShelterList(ShelterDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getShelterList Start!");

        List<ShelterDTO> rDTO = null;

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("6110000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("3220000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

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

        log.info(this.getClass().getName() + ".getShelterList End!");

        return rDTO;
    }

    private List<Map<String, Object>> parseAnimalData(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        JsonNode items = root.path("response").path("body").path("items").path("item");

        List<Map<String, Object>> result = new ArrayList<>();
        for (JsonNode item : items) {
            result.add(objectMapper.convertValue(item, Map.class));
        }
        return result;
    }

    private List<Map<String, Object>> parseShelterData(String response) throws Exception {
        JsonNode root = objectMapper.readTree(response);
        JsonNode items = root.path("response").path("body").path("items").path("item");

        List<Map<String, Object>> result = new ArrayList<>();
        for (JsonNode item : items) {
            result.add(objectMapper.convertValue(item, Map.class));
        }
        return result;
    }
}


