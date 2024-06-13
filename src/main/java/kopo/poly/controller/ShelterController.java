package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.service.impl.ShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequestMapping(value = "/shelter")
@RequiredArgsConstructor
@Controller
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping(value="shelterList")
    public String getShelterList(HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) throws Exception {

        log.info(this.getClass().getName() + ".getShelterListController Start!");

        log.info(this.getClass().getName() + ".getShelterListController End!");

        return "shelter/shelterList";
    }
}
