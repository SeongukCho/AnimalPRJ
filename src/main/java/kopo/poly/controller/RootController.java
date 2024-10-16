package kopo.poly.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RootController {

    @GetMapping(value = "/")
    public String redirectIndex() {

        log.info(this.getClass().getName() + ".redirectIndex Start!");

        log.info(this.getClass().getName() + ".redirectIndex End!");

        return "redirect:/main";
    }

}