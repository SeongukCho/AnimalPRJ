package kopo.poly.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final IUserInfoService userInfoService;

    @GetMapping(value = "/main")
    public String Main(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".main Start!!!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        return "main";
    }
}