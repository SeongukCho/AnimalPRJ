package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.IShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final MongoTemplate mongodb;

//    @Scheduled(cron = "0 0 0 1 * ?")
//    @Scheduled(fixedRate = 1000000)
    @Override
    public void saveShelterList() {

        log.info(this.getClass().getName() + ".getShelterList Start!");

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/animalShelterSrvc/shelterInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8")); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*보호센터등록번호*/
            urlBuilder.append("&" + URLEncoder.encode("care_nm", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*동물보호센터명*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수 (1,000 이하)*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/

            log.info("urlBuilder : " + urlBuilder);

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

//            System.out.println(sb.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(sb.toString());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<ShelterDTO> shelterList = new ArrayList<>();
            if (itemsNode.isArray() && !itemsNode.isEmpty()) {
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

                for (ShelterDTO shelter : shelterList) {
                    mongodb.save(shelter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("error : " + e);
        }

        log.info(this.getClass().getName() + ".getShelterList End!");
    }

    @Override
    public Page<ShelterDTO> getShelterListAll(ShelterDTO pDTO, Pageable pageable) throws Exception {

        log.info(this.getClass().getName() + ".getShelterList Start!");

        Query query = new Query().with(pageable).with(Sort.by(Sort.Order.desc("happenDt")));

        List<ShelterDTO> shelterList = mongodb.find(query, ShelterDTO.class);

        log.info("shelterList : " + shelterList);

        long total = mongodb.count(query.skip(-1).limit(-1), ShelterDTO.class); // 전체 데이터 수를 가져옴

        log.info("total data : " + total);

        log.info(this.getClass().getName() + ".getShelterList End!");

        return new PageImpl<>(shelterList, pageable, total);
    }

    @Override
    public ShelterDTO getShelterInfo(ShelterDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getShelterInfoService Start!");

        // MongoDB 쿼리 생성
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(pDTO.id()));

        // MongoDB에서 조회
        ShelterDTO rDTO = mongodb.findOne(query, ShelterDTO.class);
        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".getShelterInfoService End!");

        return rDTO;
    }
}
