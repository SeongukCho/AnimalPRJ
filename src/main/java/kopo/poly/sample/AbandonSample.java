package kopo.poly.sample;
/* Java 1.8 샘플 코드 */


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.AnimalDTO;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbandonSample {
    public static void main(String[] args) throws IOException {
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(pDTO.bgnde() != null ? pDTO.bgnde() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(pDTO.endde() != null ? pDTO.endde() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(pDTO.upkind() != null ? pDTO.upkind() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode(pDTO.kind() != null ? pDTO.kind() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(pDTO.uprCd() != null ? pDTO.uprCd() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(pDTO.orgCd() != null ? pDTO.orgCd() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode(pDTO.careRegNo() != null ? pDTO.careRegNo() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(pDTO.state() != null ? pDTO.state() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode(pDTO.neuterYnR() != null ? pDTO.neuterYnR() : "", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("20", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
//
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//
//        BufferedReader rd;
//        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//
//        System.out.println(sb.toString());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(sb.toString());
//        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
//
//        List<AnimalDTO> animalList = new ArrayList<>();
//        if (itemsNode.isArray()) {
//            for (JsonNode itemNode : itemsNode) {
//                AnimalDTO animal = AnimalDTO.builder()
//                        .desertionNo(itemNode.path("desertionNo").asLong())
//                        .fileName(itemNode.path("fileName").asText())
//                        .happenDt(itemNode.path("happenDt").asLong())
//                        .happenPlace(itemNode.path("happenPlace").asText())
//                        .kindCd(itemNode.path("kindCd").asText())
//                        .colorCd(itemNode.path("colorCd").asText())
//                        .age(itemNode.path("age").asText())
//                        .weight(itemNode.path("weight").asText())
//                        .noticeNo(itemNode.path("noticeNo").asText())
//                        .noticeSdt(itemNode.path("noticeSdt").asLong())
//                        .noticeEdt(itemNode.path("noticeEdt").asLong())
//                        .popfile(itemNode.path("popfile").asText())
//                        .processState(itemNode.path("processState").asText())
//                        .sexCd(itemNode.path("sexCd").asText())
//                        .neuterYn(itemNode.path("neuterYn").asText())
//                        .specialMark(itemNode.path("specialMark").asText())
//                        .careNm(itemNode.path("careNm").asText())
//                        .careTel(itemNode.path("careTel").asText())
//                        .careAddr(itemNode.path("careAddr").asText())
//                        .orgNm(itemNode.path("orgNm").asText())
//                        .chargeNm(itemNode.path("chargeNm").asText())
//                        .officetel(itemNode.path("officetel").asLong())
//                        .noticeComment(itemNode.path("noticeComment").asText())
//                        .build();
//
//                animalList.add(animal);
//
//            }
//        }
    }
}