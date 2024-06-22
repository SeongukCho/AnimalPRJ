package kopo.poly.service.impl;

import kopo.poly.dto.AnimalDTO;
import kopo.poly.service.IAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimalService implements IAnimalService {

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final MongoTemplate mongodb;

    /*@Override
    public List<AnimalDTO> searchAnimalList(AnimalDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".searchAnimalList Start!");

        int pageNo = 1;
        boolean hasMorePages = true;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yy.MM.dd");

        try {
            while (hasMorePages) {
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
                urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode(pDTO.neuterYn() != null ? pDTO.neuterYn() : "", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); // 배치 크기를 100으로 설정
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

                log.info("urlBuilder : " + urlBuilder);

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

//                System.out.println(sb.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(sb.toString());
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                List<AnimalDTO> animalList = new ArrayList<>();
                if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                    for (JsonNode itemNode : itemsNode) {
                        String kindCd = itemNode.path("kindCd").asText().replaceAll("\\[.*?\\]\\s*", "");
                        String happenDt = formatDateString(itemNode.path("happenDt").asText(), inputDateFormat, outputDateFormat);
                        String noticeSdt = formatDateString(itemNode.path("noticeSdt").asText(), inputDateFormat, outputDateFormat);
                        String noticeEdt = formatDateString(itemNode.path("noticeEdt").asText(), inputDateFormat, outputDateFormat);

                        AnimalDTO animal = AnimalDTO.builder()
                                .desertionNo(itemNode.path("desertionNo").asLong())
                                .fileName(itemNode.path("fileName").asText())
                                .happenDt(happenDt)
                                .happenPlace(itemNode.path("happenPlace").asText())
                                .kindCd(kindCd)
                                .colorCd(itemNode.path("colorCd").asText())
                                .age(itemNode.path("age").asText())
                                .weight(itemNode.path("weight").asText())
                                .noticeNo(itemNode.path("noticeNo").asText())
                                .noticeSdt(noticeSdt)
                                .noticeEdt(noticeEdt)
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
                                .build();

                        animalList.add(animal);
                    }

                    pageNo++;

                } else {
                    hasMorePages = false; // 더 이상 페이지가 없으면 루프 종료
                }
                log.info(this.getClass().getName() + ".searchAnimalList Service End!");
                return animalList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error : " + e);
        }
    }*/

    public void saveAnimal(AnimalDTO animal, String collectionName) {
        mongodb.save(animal, collectionName);
    }

    @Scheduled(cron = "0 0 8-20/3 * * ?")
    @Override
    public void saveAnimalList() {

        log.info(this.getClass().getName() + ".saveAnimalList Service Start!");

        int pageNo = 1;
        boolean hasMorePages = true;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yy.MM.dd");

        try {
            while (hasMorePages) {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
                urlBuilder.append("?" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); // 배치 크기를 100으로 설정
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));

                log.info("urlBuilder : " + urlBuilder);

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

//                System.out.println(sb.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(sb.toString());
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");


                List<AnimalDTO> animalList = new ArrayList<>();
                if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                    for (JsonNode itemNode : itemsNode) {
                        String kindCd = itemNode.path("kindCd").asText().replaceAll("\\[.*?\\]\\s*", "");
                        String happenDt = formatDateString(itemNode.path("happenDt").asText(), inputDateFormat, outputDateFormat);
                        String noticeSdt = formatDateString(itemNode.path("noticeSdt").asText(), inputDateFormat, outputDateFormat);
                        String noticeEdt = formatDateString(itemNode.path("noticeEdt").asText(), inputDateFormat, outputDateFormat);

                        AnimalDTO animal = AnimalDTO.builder()
                                .desertionNo(itemNode.path("desertionNo").asLong())
                                .fileName(itemNode.path("fileName").asText())
                                .happenDt(happenDt)
                                .happenPlace(itemNode.path("happenPlace").asText())
                                .kindCd(kindCd)
                                .colorCd(itemNode.path("colorCd").asText())
                                .age(itemNode.path("age").asText())
                                .weight(itemNode.path("weight").asText())
                                .noticeNo(itemNode.path("noticeNo").asText())
                                .noticeSdt(noticeSdt)
                                .noticeEdt(noticeEdt)
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
                                .build();

                        animalList.add(animal);
                    }

//                    mongodb.insertAll(animalList);
                    for (AnimalDTO animal : animalList) {
                        mongodb.save(animal);
                    }

                    pageNo++;

                } else {
                    hasMorePages = false; // 더 이상 페이지가 없으면 루프 종료
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error : " + e);
        }

        log.info(this.getClass().getName() + ".saveAnimalList Service End!");
    }

    @Scheduled(cron = "0 30 4 * * ?")
    @Override
    public void saveNoticeAnimal() {

        log.info(this.getClass().getName() + ".saveAnimalList Service Start!");

        int pageNo = 1;
        boolean hasMorePages = true;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yy.MM.dd");

        try {
            while (hasMorePages) {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
                urlBuilder.append("?" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode("notice", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); // 배치 크기를 100으로 설정
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));

                log.info("urlBuilder : " + urlBuilder);

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

//                System.out.println(sb.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(sb.toString());
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                String collectionName = "noticeAnimal";

                List<AnimalDTO> animalList = new ArrayList<>();
                if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                    for (JsonNode itemNode : itemsNode) {
                        String kindCd = itemNode.path("kindCd").asText().replaceAll("\\[.*?\\]\\s*", "");
                        String happenDt = formatDateString(itemNode.path("happenDt").asText(), inputDateFormat, outputDateFormat);
                        String noticeSdt = formatDateString(itemNode.path("noticeSdt").asText(), inputDateFormat, outputDateFormat);
                        String noticeEdt = formatDateString(itemNode.path("noticeEdt").asText(), inputDateFormat, outputDateFormat);

                        AnimalDTO animal = AnimalDTO.builder()
                                .desertionNo(itemNode.path("desertionNo").asLong())
                                .fileName(itemNode.path("fileName").asText())
                                .happenDt(happenDt)
                                .happenPlace(itemNode.path("happenPlace").asText())
                                .kindCd(kindCd)
                                .colorCd(itemNode.path("colorCd").asText())
                                .age(itemNode.path("age").asText())
                                .weight(itemNode.path("weight").asText())
                                .noticeNo(itemNode.path("noticeNo").asText())
                                .noticeSdt(noticeSdt)
                                .noticeEdt(noticeEdt)
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
                                .build();

                        animalList.add(animal);
                    }

//                    mongodb.insertAll(animalList);
                    for (AnimalDTO animal : animalList) {
                        saveAnimal(animal, collectionName);
                    }

                    pageNo++;

                } else {
                    hasMorePages = false; // 더 이상 페이지가 없으면 루프 종료
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error : " + e);
        }

        log.info(this.getClass().getName() + ".saveAnimalList Service End!");
    }

    @Scheduled(cron = "0 40 4 * * ?")
    @Override
    public void saveProtectAnimal() {

        log.info(this.getClass().getName() + ".saveAnimalList Service Start!");

        int pageNo = 1;
        boolean hasMorePages = true;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yy.MM.dd");

        try {
            while (hasMorePages) {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
                urlBuilder.append("?" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode("protect", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); // 배치 크기를 100으로 설정
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));

                log.info("urlBuilder : " + urlBuilder);

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

//                System.out.println(sb.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(sb.toString());
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                String collectionName = "protectAnimal";

                List<AnimalDTO> animalList = new ArrayList<>();
                if (itemsNode.isArray() && !itemsNode.isEmpty()) {
                    for (JsonNode itemNode : itemsNode) {
                        String kindCd = itemNode.path("kindCd").asText().replaceAll("\\[.*?\\]\\s*", "");
                        String happenDt = formatDateString(itemNode.path("happenDt").asText(), inputDateFormat, outputDateFormat);
                        String noticeSdt = formatDateString(itemNode.path("noticeSdt").asText(), inputDateFormat, outputDateFormat);
                        String noticeEdt = formatDateString(itemNode.path("noticeEdt").asText(), inputDateFormat, outputDateFormat);

                        AnimalDTO animal = AnimalDTO.builder()
                                .desertionNo(itemNode.path("desertionNo").asLong())
                                .fileName(itemNode.path("fileName").asText())
                                .happenDt(happenDt)
                                .happenPlace(itemNode.path("happenPlace").asText())
                                .kindCd(kindCd)
                                .colorCd(itemNode.path("colorCd").asText())
                                .age(itemNode.path("age").asText())
                                .weight(itemNode.path("weight").asText())
                                .noticeNo(itemNode.path("noticeNo").asText())
                                .noticeSdt(noticeSdt)
                                .noticeEdt(noticeEdt)
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
                                .build();

                        animalList.add(animal);
                    }

//                    mongodb.insertAll(animalList);
                    for (AnimalDTO animal : animalList) {
                        saveAnimal(animal, collectionName);
                    }

                    pageNo++;

                } else {
                    hasMorePages = false; // 더 이상 페이지가 없으면 루프 종료
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error : " + e);
        }

        log.info(this.getClass().getName() + ".saveAnimalList Service End!");
    }

    public void getKind(AnimalDTO pDTO) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/kind"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=%2BudC8qehuL7hIRzNnxnOD5CaIDy7poBoxA5JJleBVJgdhdDXXuzGvk3vJWMjFUhlnyJaLvSiGhI0BbGLqXNG7A%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("up_kind_cd","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*축종코드 (개 : 417000, 고양이 : 422400, 기타 : 429900) (입력 시 데이터 O, 미입력 시 데이터 X)*/
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
    }


    @Override
    public Page<AnimalDTO> getAnimalListAll(AnimalDTO pDTO, Pageable pageable, String collectionName) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalList Start!");

        Query query = new Query().with(pageable).with(Sort.by(Sort.Order.desc("happenDt")));

        List<AnimalDTO> animalList = mongodb.find(query, AnimalDTO.class, collectionName);

        log.info("animalList : " + animalList);

        long total = mongodb.count(query.skip(-1).limit(-1), AnimalDTO.class); // 전체 데이터 수를 가져옴

        log.info("total data : " + total);

        log.info(this.getClass().getName() + ".getAnimalList End!");

        return new PageImpl<>(animalList, pageable, total);
    }

    @Override
    public AnimalDTO getAnimalInfo(AnimalDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalInfoService Start!");

        // MongoDB 쿼리 생성
        Query query = new Query();
        query.addCriteria(Criteria.where("desertionNo").is(pDTO.desertionNo()));

        // MongoDB에서 조회
        AnimalDTO rDTO = mongodb.findOne(query, AnimalDTO.class);

        log.info(this.getClass().getName() + ".getAnimalInfoService End!");

        return rDTO;
    }

    private String formatDateString(String dateStr, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            log.error("Date parsing error: " + dateStr, e);
            return dateStr;
        }
    }
}


