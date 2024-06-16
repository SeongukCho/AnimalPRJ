package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함

    private final IUserInfoService userInfoService;


    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info(this.getClass().getName() + ".user/userRegForm Start!");

        log.info(this.getClass().getName() + ".user/userRegForm End!");

        return "user/userRegForm";
    }

    @GetMapping(value = "searchUserId")
    public String searchUserId() {

        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/searchUserId";
    }

    @GetMapping(value = "searchPassword")
    public String searchPassword() {

        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        log.info(this.getClass().getName() + ".user/searchPassword End!");

        return "user/searchPassword";
    }

    @GetMapping(value = "newPasswordResult")
    public String newPasswordResult() {

        log.info(this.getClass().getName() + ".user/newPasswordResult Start!");

        log.info(this.getClass().getName() + ".user/newPasswordResult End!");

        return "user/newPasswordResult";
    }

    @PostMapping(value = "newPassword")
    public String searchPasswordProc(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/newPassword Start!");

        String userId = CmmUtil.nvl((request.getParameter("userId"))); // 아이디
//        String userName = CmmUtil.nvl((request.getParameter("userName"))); // 이름
        String email = CmmUtil.nvl((request.getParameter("email"))); // 이메일

        log.info("userId : " + userId);
//        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
//                .userName(userName)
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        // 비밀번호 찾기 가능한지 확인하기
        int res = userInfoService.searchUserIdOrPasswordPro(pDTO);

        // 비밀번호 재생성 화면은 보안을 위해 반드시 NEW_PASSWORD 세션이 존재해야 접속 가능하도록 구현
        // userId 값을 넣은 이유는 비밀번호 재설정하는 newPasswordProc 함수에서 사용하기 위함

        if (res > 0) {

            session.setAttribute("NEW_PASSWORD", userId);

            log.info(this.getClass().getName() + ".user/newPassword success End!");

            return "user/newPassword";
        }

        else {

            session.setAttribute("NEW_PASSWORD", "");

            log.info(this.getClass().getName() + ".user/newPassword fail End!");

            return "user/newPassword";
        }
    }

    @PostMapping(value = "updatePassword")
    public String newPasswordProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/updatePassword Start!");

        String msg = ""; // 웹에 보여줄 메세지

        // 정상적인 접근인지 체크
        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if(newPassword.length() > 0) { //정상접근
            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(newPassword)
                    .password(EncryptUtil.encHashSHA256(password))
                    .build();

            int res = userInfoService.updatePassword(pDTO);

            // 비밀번호 재생성 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            if(res > 0) {
                msg = "비밀번호가 재설정되었습니다.";
            } else {
                msg = "비밀번호 변경에 문제가 생겼습니다. 다시 시도해주세요.";
            }

        } else { //비정상 접근
            msg = "비정상 접근입니다.";
        }

        session.setAttribute("MSG", msg);

        log.info("msg : " + msg);

        log.info(this.getClass().getName() + ".user/updatePassword End!");

        return "/user/newPasswordResult";
    }



    @ResponseBody
    @PostMapping(value="getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId : " + userId);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String nickName = CmmUtil.nvl(request.getParameter("nickName"));
        String password = CmmUtil.nvl(request.getParameter("password"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("nickName : " + nickName);
        log.info("password : " + password);
        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .nickName(nickName)
                .password(EncryptUtil.encHashSHA256(password))
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        int res = userInfoService.insertUserInfo(pDTO);

        log.info("회원가입 결과(res) : " + res);

        if (res == 1) {
            msg = "회원가입되었습니다.";

        } else if (res == 2) {
            msg = "이미 가입된 아이디입니다.";
        } else {
            msg = "오류로 인해 회원가입이 실패했습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return dto;
    }

    @GetMapping(value = "login")
    public String login() {

        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return  "user/login";
    }

    /**
     * 비밀번호 변경 고유 세션 삭제 후 이동
     */
    @GetMapping(value = "passProc")
    public String passProc(HttpSession session) {

        log.info(this.getClass().getName() + ".user/passProc Start!");

        // 비밀번호 재생성 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        log.info(this.getClass().getName() + ".user/passProc End!");

        return  "redirect:/user/login";
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginProc Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("userId : " +  userId);
        log.info("password : " +  password);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .password(EncryptUtil.encHashSHA256(password)).build();

        int res = userInfoService.getUserLogin(pDTO);
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                        .orElseGet(() -> UserInfoDTO.builder().build());

        log.info("res : " +res);

        if (res == 1) {

            msg = "로그인 성공!";
            session.setAttribute("SS_USER_ID", userId);
            session.setAttribute("SS_USER_NAME", rDTO.nickName());

            log.info("SS_USER_ID : " + rDTO.userId());
            log.info("SS_USER_NAME : " + rDTO.userName());

        } else {
            msg = "아이디와 비밀번호가 올바르지 않습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();
        log.info(this.getClass().getName() + ".loginProc End!");

        return dto;
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {
        log.info(this.getClass().getName() + ".user/loginSuccess Start!");

        log.info(this.getClass().getName() + ".user/loginSuccess End!");

        return "user/loginSuccess";

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".logout Start!");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        log.info(this.getClass().getName() + ".logout End!");

        return "main/main"; // 로그아웃 후 이동할 URL 설정
    }

    /**
     * ###########################################################################
     *
     *  이메일 있는지 체크
     *
     * ###########################################################################
     */

    /**
     * 회원 가입 전 이메일 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     * 유효한 이메일인지 확인하기 위해 입력된 이메일에 인증번호 포함하여 메일 발송
     */
    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        //이메일을 통해 중복된 이메일인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "sendEmailAuth")
    public UserInfoDTO SendEmailAuth(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO =  UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.sendEmailAuth(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());
        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

//    @ResponseBody
//    @PostMapping(value = "signUpEmailExists")
//    public UserInfoDTO signUpEmailExists(HttpServletRequest request) throws Exception {
//        log.info(this.getClass().getName() + ".signUpEmailExists Start!");
//
//        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일
//
//        log.info("email : " + email);
//
//        UserInfoDTO pDTO = UserInfoDTO.builder()
//                .email(EncryptUtil.encAES128CBC(email))
//                .build();
//
//        //이메일을 통해 중복된 이메일인지 조회
//        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
//                .orElseGet(() -> UserInfoDTO.builder().build());
//
//        String existsYn = rDTO.existsYn();
//
//        log.info("existsYn : " + existsYn);
//
//        if (existsYn == "N") {
//
//            //이메일을 통해 중복된 이메일인지 조회
//            rDTO = Optional.ofNullable(userInfoService.sendSignUpEmailAuth(pDTO))
//                    .orElseGet(() -> UserInfoDTO.builder().build());
//
//        }
//
//        log.info(this.getClass().getName() + ".signUpEmailExists End!");
//
//        return rDTO;
//    }

    // myPage

    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPage Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);

        } else {

            return "/user/login";

        }

        log.info(this.getClass().getName() + ".user/myPage End!");

        return  "user/myPage";

    }



    // 유저 정보 수정 페이지 이동

    @GetMapping(value = "myPageEdit")
    public String myPageEdit(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPageEdit Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);

        } else {

            return "/user/login";

        }

        log.info(this.getClass().getName() + ".user/myPageEdit End!");

        return  "user/myPageEdit";

    }

    // 비밀 번호 변경 (MyPage) 페이지 이동
    @GetMapping(value = "myNewPassword")
    public String myNewPassword(HttpSession session) {

        log.info(this.getClass().getName() + ".user/myNewPassword Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if (userId.length() > 0) {

        } else {


            return "user/login";

        }

        return "user/myNewPassword";

    }


    @ResponseBody
    @PostMapping(value = "myUpdatePassword")
    public MsgDTO myUpdatePassword(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/myUpdatePassword Start!");

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        String msg = ""; // 웹에 보여줄 메세지
        int result = 0;

        if(userId.length() > 0) { //정상접근

            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .password(EncryptUtil.encHashSHA256(password))
                    .build();

            int res = userInfoService.updatePassword(pDTO);


            if(res > 0) {

                msg = "비밀번호가 재설정되었습니다.";
                result = 0;

            } else {

                msg = "비밀번호 변경에 문제가 생겼습니다. 다시 시도해주세요.";

                result = 1;

            }

        } else { //비정상 접근

            msg = "비정상 접근입니다.";
            result = 2;

        }

        MsgDTO mDTO = MsgDTO.builder()
                .msg(msg)
                .result(result)
                .build();

        log.info("msg : " + msg);

        log.info(this.getClass().getName() + ".user/myUpdatePassword End!");

        return mDTO;
    }


    // 유저 정보 수정
    @ResponseBody
    @PostMapping(value = "updateUserInfo")
    public MsgDTO userInfoUpdate(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        String msg;

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + (email));

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .email(email)
                .build();

        int res = userInfoService.updateUserInfo(pDTO);

        log.info("호윈 정보 수정 결과(res) : " + res);

        if (res == 1) {
            msg = "회원 정보 수정되었습니다..";

        } else {

            msg = "오류로 인해 회원 정보 수정에 실패했습니다.";

        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".updateUserInfo End!");

        return dto;
    }


//    // 회원 탈퇴 페이지
//    @GetMapping(value = "withdrawal")
//    public String Withdrawal(HttpSession session) {
//
//        log.info(this.getClass().getName() + ".user/withdrawal Start!");
//
//        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
//
//        if(userId != null){
//
//        } else {
//            return "user/login";
//        }
//
//        log.info(this.getClass().getName() + ".user/withdrawal End!");
//
//        return "user/withdrawal";
//    }


    /**
     * 유저 정보 삭제
     */
    @ResponseBody
    @PostMapping(value = "deleteUserInfo")
    public MsgDTO DeleteUserInfo(HttpSession session) {

        log.info(this.getClass().getName() + ".deleteUserInfo Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 글번호(PK)

            log.info("userId : " + userId);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            // 게시글 삭제하기 DB
            userInfoService.deleteUserInfo(pDTO);

            session.invalidate();

            msg = "탈퇴하였습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".deleteUserInfo End!");

        }

        return dto;
    }

}