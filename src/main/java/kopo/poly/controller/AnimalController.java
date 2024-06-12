package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.BoardDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.impl.AnimalService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/animal")
@RequiredArgsConstructor
@Controller
public class AnimalController {

    private final AnimalService animalService;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @GetMapping(value = "animalList")
    public String AnimalList(HttpServletRequest request, ModelMap model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListController Start!");

        String bgnde = CmmUtil.nvl(request.getParameter("bgnde"));
        String endde = CmmUtil.nvl(request.getParameter("endde"));
        String upkind = CmmUtil.nvl(request.getParameter("upkind"));
        String kind = CmmUtil.nvl(request.getParameter("kind"));
        String upr_cd = CmmUtil.nvl(request.getParameter("upr_cd"));
        String org_cd = CmmUtil.nvl(request.getParameter("org_cd"));
        String care_reg_no = CmmUtil.nvl(request.getParameter("care_reg_no"));
        String state = CmmUtil.nvl(request.getParameter("state"));
        String neuter_yn = CmmUtil.nvl(request.getParameter("neuter_yn"));

        AnimalDTO pDTO = AnimalDTO.builder()
                .bgnde(bgnde)
                .endde(endde)
                .upkind(upkind)
                .kind(kind)
                .uprCd(upr_cd)
                .orgCd(org_cd)
                .careRegNo(care_reg_no)
                .state(state)
                .neuterYnR(neuter_yn)
                .build();

        List<AnimalDTO> rList = animalService.getAnimalList(pDTO);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", rList.getTotalPages());
//        model.addAttribute("totalItems", rList.getTotalElements());

        log.info(this.getClass().getName() + ".getAnimalListController End!");

        return "animal/animalList";
    }

    @GetMapping(value = "animalInfo")
    public List<AnimalDTO> getAnimalInfo(HttpServletRequest request, ModelMap model,
                                         @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalInfoController Start!");

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
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

//        String bgnde = CmmUtil.nvl(request.getParameter("bgnde"));
//        String endde = CmmUtil.nvl(request.getParameter("endde"));
//        String upkind = CmmUtil.nvl(request.getParameter("upkind"));
//        String kind = CmmUtil.nvl(request.getParameter("kind"));
//        String upr_cd = CmmUtil.nvl(request.getParameter("upr_cd"));
//        String org_cd = CmmUtil.nvl(request.getParameter("org_cd"));
//        String care_reg_no = CmmUtil.nvl(request.getParameter("care_reg_no"));
//        String state = CmmUtil.nvl(request.getParameter("state"));
//        String neuter_yn = CmmUtil.nvl(request.getParameter("neuter_yn"));
//
//        AnimalDTO pDTO = AnimalDTO.builder()
//                .bgnde(bgnde)
//                .endde(endde)
//                .upkind(upkind)
//                .kind(kind)
//                .upr_cd(upr_cd)
//                .org_cd(org_cd)
//                .care_reg_no(care_reg_no)
//                .state(state)
//                .neuter_yn(neuter_yn)
//                .build();
//
////        AnimalDTO rDTO = Optional.ofNullable(animalService.fetchAnimalData(pDTO))
////                .orElseGet(() -> AnimalDTO.builder().build());

        log.info(this.getClass().getName() + ".getAnimalInfoController End!");

        return null;
    }

    @GetMapping(value="shelterList")
    public String getShelterList(HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getShelterListController Start!");

        log.info(this.getClass().getName() + ".getShelterListController End!");

        return "animal/shelterList";
    }
}