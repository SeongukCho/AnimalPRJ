package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.BoardDTO;
import kopo.poly.dto.CommentDTO;
import kopo.poly.service.impl.AnimalService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/animal")
@RequiredArgsConstructor
@Controller
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping(value = "animalList")
    public List<AnimalDTO> getAnimalListAll(AnimalDTO pDTO,ModelMap model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListAllController Start!");

        List<AnimalDTO> rList = animalService.getAnimalListAll(pDTO);

        log.info("rList : " + rList);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".getAnimalListAllController End!");

        return rList;
    }

    @Transactional
    @GetMapping
    public String getAnimalSearch(HttpServletRequest request, ModelMap model) throws Exception {

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

        List<AnimalDTO> rList = animalService.getAnimalListAll(pDTO);

        log.info("rList : " + rList);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        return "animal/animalSearch";
    }

    @GetMapping(value = "animalInfo")
    public String getAnimalInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalInfoController Start!");

        String desertionNo = CmmUtil.nvl(request.getParameter("desertionNo"), "0"); // 공고번호

        log.info("desertionNo : " + desertionNo);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        AnimalDTO pDTO = AnimalDTO.builder()
                .desertionNo(Long.parseLong(desertionNo))
                .build();

//        List<AnimalDTO> rDTO = animalService.getAnimalInfo(pDTO);

        // 조회된 리스트 결과값 넣어주기
//        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".getAnimalInfoController End!");

        return "animal/animalInfo";
    }

    @GetMapping(value = "shelterList")
    public String getShelterList(HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getShelterListController Start!");

        log.info(this.getClass().getName() + ".getShelterListController End!");

        return "animal/shelterList";
    }

    @GetMapping(value = "shelterInfo")
    public String getShelterInfo(HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getShelterInfoController Start!");


        log.info(this.getClass().getName() + ".getShelterInfoController End!");

        return "animal/shelterInfo";
    }
}