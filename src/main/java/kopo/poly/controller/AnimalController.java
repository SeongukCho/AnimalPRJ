package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.service.IWeatherService;
import kopo.poly.service.impl.AnimalService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequestMapping(value = "/animal")
@RequiredArgsConstructor
@Controller
public class AnimalController {

    private final AnimalService animalService;
    private final IWeatherService weatherService;

    /*@GetMapping(value = "animalList")
    public List<AnimalDTO> getAnimalListAll(AnimalDTO pDTO,ModelMap model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListAllController Start!");

        List<AnimalDTO> rList = animalService.getAnimalListAll(pDTO);

        log.info("rList : " + rList);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".getAnimalListAllController End!");

        return rList;
    }*/

    @GetMapping(value = "animalList")
    public String getAnimalListAll(AnimalDTO pDTO, ModelMap model, HttpSession session,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "40") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListAllController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("pageable : " + pageable);

        Page<AnimalDTO> animalPage = animalService.getAnimalListAll(pDTO, pageable, "animalDTO");

        List<AnimalDTO> rList = animalPage.getContent(); // 페이징된 데이터 리스트

        log.info("rList : " + rList);
        log.info("animalPage : " + animalPage);
        log.info("animalPage.getTotalPages() : " + animalPage.getTotalPages());

        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", animalPage.getTotalPages());

// 페이지 번호 범위 계산
        int startPage = Math.max(1, page - 4);
        int endPage = Math.min(animalPage.getTotalPages(), page + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        log.info(this.getClass().getName() + ".getAnimalListAllController End!");

        return "animal/animalList"; // 적절한 뷰 이름으로 변경
    }

    @GetMapping(value = "protectAnimal")
    public String getProtectAnimal(AnimalDTO pDTO, ModelMap model, HttpSession session,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "40") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListAllController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("pageable : " + pageable);

        Page<AnimalDTO> animalPage = animalService.getAnimalListAll(pDTO, pageable, "protectAnimal");

        List<AnimalDTO> rList = animalPage.getContent(); // 페이징된 데이터 리스트

        log.info("rList : " + rList);
        log.info("animalPage : " + animalPage);
        log.info("animalPage.getTotalPages() : " + animalPage.getTotalPages());

        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", animalPage.getTotalPages());

// 페이지 번호 범위 계산
        int startPage = Math.max(1, page - 4);
        int endPage = Math.min(animalPage.getTotalPages(), page + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        log.info(this.getClass().getName() + ".getAnimalListAllController End!");

        return "animal/protectAnimal"; // 적절한 뷰 이름으로 변경
    }

    @GetMapping(value = "noticeAnimal")
    public String getNoticeAnimalList(AnimalDTO pDTO, ModelMap model, HttpSession session,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "40") int size) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalListAllController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("pageable : " + pageable);

        Page<AnimalDTO> animalPage = animalService.getAnimalListAll(pDTO, pageable, "noticeAnimal");

        List<AnimalDTO> rList = animalPage.getContent(); // 페이징된 데이터 리스트

        log.info("rList : " + rList);
        log.info("animalPage : " + animalPage);
        log.info("animalPage.getTotalPages() : " + animalPage.getTotalPages());

        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", animalPage.getTotalPages());

// 페이지 번호 범위 계산
        int startPage = Math.max(1, page - 4);
        int endPage = Math.min(animalPage.getTotalPages(), page + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        log.info(this.getClass().getName() + ".getAnimalListAllController End!");

        return "animal/noticeAnimal"; // 적절한 뷰 이름으로 변경
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

//        List<AnimalDTO> rList = animalService.searchAnimalList(pDTO);
//
//        log.info("rList : " + rList);
//
//         조회된 리스트 결과값 넣어주기
//        model.addAttribute("rList", rList);

        return "animal/animalList";
    }

    @GetMapping(value = "animalInfo/{desertionNo}")
    public String getAnimalInfo(@PathVariable String desertionNo, HttpServletRequest request,HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".getAnimalInfoController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");
        model.addAttribute("userId", userId);

        log.info("desertionNo : " + desertionNo);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        AnimalDTO pDTO = AnimalDTO.builder()
                .desertionNo(Long.parseLong(desertionNo))
                .build();

        AnimalDTO rDTO = animalService.getAnimalInfo(pDTO);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".getAnimalInfoController End!");

        return "animal/animalInfo";
    }
}