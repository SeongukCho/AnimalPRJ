package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.IShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShelterService implements IShelterService {

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Override
    public List<ShelterDTO> getShelterList(ShelterDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getShelterList Start!");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/animalShelterSrvc/shelterInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("care_reg_no","UTF-8") + "=" + URLEncoder.encode(pDTO.careRegNo() != null ? pDTO.careRegNo() : "", "UTF-8")); /*보호센터등록번호*/
        urlBuilder.append("&" + URLEncoder.encode("care_nm","UTF-8") + "=" + URLEncoder.encode(pDTO.careNm() != null ? pDTO.careNm() : "", "UTF-8")); /*동물보호센터명*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수 (1,000 이하)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
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

        List<ShelterDTO> shelterList = new ArrayList<>();
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                ShelterDTO shelter = ShelterDTO.builder()
                        .careNm(itemNode.path("careNm").asText())
                        .orgNm(itemNode.path("orgNm").asText())
                        .divisionNm(itemNode.path("divisionNm").asText())
                        .saveTrgtAnimal(itemNode.path("saveTrgtAnimal").asText())
                        .careAddr(itemNode.path("careAddr").asText())
                        .jibunAddr(itemNode.path("jibunAddr").asText())
                        .lat(itemNode.path("lat").asLong())
                        .lng(itemNode.path("lng").asLong())
                        .dsignationDate(itemNode.path("dsignationDate").asText())
                        .weekOprStime(itemNode.path("weekOprStime").asText())
                        .weekOprEtime(itemNode.path("weekOprEtime").asText())
                        .weekCellStime(itemNode.path("weekCellStime").asText())
                        .weekCellEtime(itemNode.path("weekCellEtime").asText())
                        .weekendOprStime(itemNode.path("weekendOprStime").asText())
                        .weekendOprEtime(itemNode.path("weekendOprEtime").asText())
                        .weekendCellStime(itemNode.path("weekendCellStime").asText())
                        .weekendCellEtime(itemNode.path("weekendCellEtime").asText())
                        .closeDay(itemNode.path("closeDay").asText())
                        .vetPersonCnt(itemNode.path("vetPersonCnt").asLong())
                        .specsPersonCnt(itemNode.path("specsPersonCnt").asLong())
                        .medicalCnt(itemNode.path("medicalCnt").asLong())
                        .breedCnt(itemNode.path("breedCnt").asLong())
                        .quarabtineCnt(itemNode.path("quarabtineCnt").asLong())
                        .feedCnt(itemNode.path("feedCnt").asLong())
                        .transCarCnt(itemNode.path("transCarCnt").asLong())
                        .careTel(itemNode.path("careTel").asText())
                        .dataStdDt(itemNode.path("dataStdDt").asText())
                        .build();

                shelterList.add(shelter);
            }
        }

        log.info("shelterList : " + shelterList);

        log.info(this.getClass().getName() + ".getShelterList End!");

        return shelterList;
    }
}
