package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/mail")
@Controller
public class MailController {


    private final IMailService mailService; // 메일 발송을 위한 서비스 객체를 사용하기

    /**
     * 메일 발송하기
     */

    @ResponseBody
    /* Ajax -> Json 형식 변경 */
    @PostMapping(value = "sendMail")
    public MsgDTO sendMail(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".sendMail Start!");

        String msg = ""; // 발송 결과 메세지

        // 웹 URL로부터 전달 받는 값들
        String toMail = CmmUtil.nvl(request.getParameter("toMail")); // 받는 사람
        String title = CmmUtil.nvl(request.getParameter("title")); // 제목
        String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

        /*
         * 들어온 값 확인
         */

        log.info("toMail : " + toMail);
        log.info("title : " + title);
        log.info("contents " + contents);

        // 메일 DTO 생성
        MailDTO pDTO = MailDTO.builder()
                .toMail(toMail)
                .title(title)
                .contents(contents)
                .build();

        //메일 발송하기
        int res = mailService.doSendMail(pDTO);

        if(res == 1) { // 메일 발송 성공
            msg = "메일 발송하였습니다.";
        } else { //메일발송 실패
            msg = "메일 발송 실패하였습니다.";
        }

        log.info(msg);

        //결과 메세지 전달하기
        MsgDTO dto = MsgDTO.builder()
                .msg(msg)
                .build();

        log.info(this.getClass().getName() + ".sendMail End!");

        return dto;
    }


}