package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.service.IAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AnimalService implements IAnimalService {

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final ObjectMapper objectMapper;

    @Override
    public List<AnimalDTO> fetchAnimalData() throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
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

        return rDTO;
    }

    @Override
    public List<AnimalDTO> fetchShelterData() throws Exception {

        List<AnimalDTO> rDTO = null;

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/shelter");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode(, "UTF-8"));
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
