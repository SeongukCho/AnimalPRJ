package kopo.poly.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.NaverDTO;
import kopo.poly.dto.TokenDTO;
import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.INaverService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NaverController {

    private final INaverService naverService;

    private final IUserInfoService userInfoService;

    private final IWeatherService weatherService;

    @GetMapping(value = "/auth/naver/callback")
    public String naverCallback(String code, HttpSession session, ModelMap model) throws Exception {

        if (code == null || code.isEmpty()) {

            return "redirect:/user/login";
        } else {

            log.info(".controller 네이버 회원가입 및 로그인 실행");

            int res; // 회원 가입 결과 /// 1 성공, 2 이미 가입

            TokenDTO tokenDTO = naverService.getAccessToken(code);

            log.info("네이버 토큰 : " + tokenDTO);
            log.info("네이버 엑세스 토큰 : " + tokenDTO.access_token());

            NaverDTO naverDTO = naverService.getNaverUserInfo(tokenDTO);

            log.info("naverDTO : " + naverDTO);

            String naverId = naverDTO.response().getId();
            String userId = "naver_" + naverId.substring(0, Math.min(naverId.length(), 10));

            String nickName = naverDTO.response().getNickname();

            log.info("네이버 아이디 : " + naverDTO.response().getId());

            // 첫 로그인시 회원가입 로직 실행
            if (Objects.equals(userInfoService.getUserIdExist(userId), "Y")) {

                log.info("첫 로그인 회원가입");

                res = userInfoService.insertSocialUser(
                        userId,
                        EncryptUtil.encAES128CBC(tokenDTO.access_token()),
                        naverDTO.response().getEmail(),
                        naverDTO.response().getNickname(),
                        naverDTO.response().getName()
                );

                if (res == 1) {
                    log.info("회원가입 성공");

                    session.setAttribute("SS_USER_ID", userId);
                    session.setAttribute("SS_USER_NAME", nickName);
                    model.addAttribute("userId", userId);

                } else {
                    log.info("회원가입 실패");
                }

            } else {
                log.info("가입 이력 존재 로그인 실행");

                session.setAttribute("SS_USER_ID", userId);
                session.setAttribute("SS_USER_NAME", nickName);
                model.addAttribute("userId", userId);

            }

            WeatherDTO wDTO = weatherService.getWeatherInfo();

            model.addAttribute("wDTO", wDTO);

            log.info(".controller 네이버 회원가입 및 로그인 종료");

            return "redirect:/main";
        }
    }
}