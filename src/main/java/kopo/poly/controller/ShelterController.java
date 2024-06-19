package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.AnimalDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.impl.ShelterService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequestMapping(value = "/shelter")
@RequiredArgsConstructor
@Controller
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping(value = "shelterList")
    public String getShelterListAll(ShelterDTO pDTO, ModelMap model, HttpSession session,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "20") int size) throws Exception {

        log.info(this.getClass().getName() + ".getShelterListAllController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("pageable : " + pageable);

        Page<ShelterDTO> shelterPage = shelterService.getShelterListAll(pDTO, pageable);

        List<ShelterDTO> rList = shelterPage.getContent(); // 페이징된 데이터 리스트

        log.info("rList : " + rList);
        log.info("shelterPage : " + shelterPage);
        log.info("shelterPage.getTotalPages() : " + shelterPage.getTotalPages());

        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", shelterPage.getTotalPages());

// 페이지 번호 범위 계산
        int startPage = Math.max(1, page - 4);
        int endPage = Math.min(shelterPage.getTotalPages(), page + 5);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        log.info(this.getClass().getName() + ".getShelterListAllController End!");

        return "shelter/shelterList"; // 적절한 뷰 이름으로 변경
    }

    @GetMapping(value = "shelterInfo/{id}")
    public String getShelterInfo(@PathVariable("id") String id, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".getShelterInfoController Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");
        model.addAttribute("userId", userId);

        log.info("id : " + id);

        ShelterDTO pDTO = ShelterDTO.builder()
                .id(id)
                .build();

        ShelterDTO rDTO = shelterService.getShelterInfo(pDTO);

        model.addAttribute("rDTO", rDTO);

        // 조회된 리스트 결과값 넣어주기

        log.info(this.getClass().getName() + ".getShelterInfoController End!");

        return "shelter/shelterInfo";
    }
}
