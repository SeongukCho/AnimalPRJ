package kopo.poly.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.service.IWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final IUserInfoService userInfoService;
    private final IWeatherService weatherService;

    @GetMapping(value = "/main")
    public String Main(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".main Start!!!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        model.addAttribute("userId", userId);

        WeatherDTO wDTO = weatherService.getWeatherInfo();

        model.addAttribute("wDTO", wDTO);

        return "main";
    }
}