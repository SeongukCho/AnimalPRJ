package kopo.poly.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/center")
@RequiredArgsConstructor
@Controller
public class CenterController {
    @GetMapping(value = "/center")
    public String CenterMap() {

        log.info(this.getClass().getName() + ".CenterMap Start!");

        log.info(this.getClass().getName() + ".CenterMap End!");

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/notice/noticeReg.html
        return "center/center";
    }
}
