package kopo.poly.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.dto.WeatherDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.service.INaverService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.service.IWeatherService;
import kopo.poly.service.impl.RecaptchaService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequestMapping(value = "user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final IUserInfoService userInfoService;
    private final UserInfoRepository userInfoRepository;
    private final RecaptchaService recaptchaService;
    private final IWeatherService weatherService;
    private final INaverService naverService;

    @Value("${v3.secretKey}")
    private String secretKey;


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
        } else {

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

        if (newPassword.length() > 0) { //정상접근
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

            if (res > 0) {
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

        return "user/newPasswordResult";
    }

    @GetMapping(value = "changePassword")
    public String changePassword(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".changePassword Controller Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        model.addAttribute("userId", userId);

        if (userId.length() > 0) {

        } else {
            return "user/login";
        }

        return "user/changePassword";
    }

    @ResponseBody
    @PostMapping(value = "changePasswordProc")
    public MsgDTO ChangePasswordProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".ChangePasswordProc Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String img;

        String msg = ""; // 웹에 보여줄 메세지
        int result = 0;

        if (userId.length() > 0) { //정상접근

            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .password(EncryptUtil.encHashSHA256(password))
                    .build();

            int res = userInfoService.updatePassword(pDTO);


            if (res > 0) {

                msg = "비밀번호가 재설정되었습니다.";
                result = 0;

            } else {

                msg = "비밀번호 변경에 실패하였습니다. 다시 시도해주세요.";

                result = 1;

            }

        } else { //비정상 접근

            msg = "비정상 접근입니다.";
            result = 2;

        }

        MsgDTO rDTO = MsgDTO.builder()
                .msg(msg)
                .result(result)
                .build();

        log.info("rDTO : " + rDTO);

        log.info(this.getClass().getName() + ".ChangePasswordProc End!");

        return rDTO;
    }


    @ResponseBody
    @PostMapping(value = "getUserIdExists")
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
    @PostMapping(value = "getNickNameExists")
    public UserInfoDTO getNickNameExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getNickNameExists Start!");

        String nickName = CmmUtil.nvl(request.getParameter("nickName"));

        log.info("nickName : " + nickName);

        UserInfoDTO pDTO = UserInfoDTO.builder().nickName(nickName).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getNickNameExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".getNickNameExists End!");

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
    public String login(HttpSession session) {

        log.info(this.getClass().getName() + ".user/login Start!");

        String userId = (String) session.getAttribute("SS_USER_ID");

        if (userId != null && userId.length() > 0) {

            return "redirect:/main";
        } else {

            log.info(this.getClass().getName() + ".user/login End!");

            return "user/login";
        }
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

        return "redirect:user/login";
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginProc Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("userId : " + userId);
        log.info("password : " + password);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .password(EncryptUtil.encHashSHA256(password)).build();

        UserInfoDTO nickDTO = userInfoService.getNickName(userId);

        String nickName = nickDTO.nickName();

        int res = userInfoService.getUserLogin(pDTO);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info("rDTO : " + rDTO);

        log.info("res : " + res);

        if (res == 1) {

            msg = "로그인 성공!";
            session.setAttribute("SS_USER_ID", userId);
            session.setAttribute("SS_USER_NAME", nickName);

            log.info("SS_USER_ID : " + userId);
            log.info("SS_USER_NAME : " + nickName);

        } else {
            msg = "아이디와 비밀번호가 올바르지 않습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();
        log.info(this.getClass().getName() + ".loginProc End!");

        return dto;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".logout Start!");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        WeatherDTO wDTO = weatherService.getWeatherInfo();

        model.addAttribute("wDTO", wDTO);

        log.info(this.getClass().getName() + ".logout End!");

        return "main"; // 로그아웃 후 이동할 URL 설정
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

        UserInfoDTO pDTO = UserInfoDTO.builder()
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

    @GetMapping(value = "myPage/{userId}")
    public String myPage(@PathVariable("userId") String userId, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPage Start!");

        String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("ssUserId : " + ssUserId);

        if (ssUserId.length() > 0) {

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(ssUserId))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);
            model.addAttribute("userId", ssUserId);

        } else {
            return "user/login";
        }

        log.info(this.getClass().getName() + ".user/myPage End!");

        return "user/myPage";
    }


    // 유저 정보 수정 페이지 이동

    @GetMapping(value = "myPageEdit")
    public String myPageEdit(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPageEdit Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("userId : " + userId);

        UserInfoDTO pDTO = userInfoService.getUserInfo(userId);

        String profilePath = pDTO.profilePath() != null && !pDTO.profilePath().isEmpty() ? pDTO.profilePath() : "/img/profile.png";

        log.info("profilePath : " + profilePath);
        log.info("userName : " + pDTO.userName());

        if (userId.length() > 0) {

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(userId))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);
            model.addAttribute("userId", userId);

            log.info(this.getClass().getName() + ".user/myPageEdit End!");

            return "user/myPageEdit";
        } else {

            return "user/login";

        }
    }

    // 유저 정보 수정

    @PostMapping(value = "updateUserInfo")
    public ResponseEntity<?> updateUserInfo(@RequestParam(value = "file", required = false) MultipartFile file,
                                            @RequestParam(value = "nickName", required = false) String nickName,
                                            HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");


        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("userId : " + userId);
        log.info("nickName : " + nickName);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .nickName(nickName)
                .build();

        userInfoService.newNickNameProc(pDTO);

        UserInfoDTO existingUserDTO = userInfoService.getUserInfo(userId);

        log.info("nickName : " + nickName);

        if (file != null && !file.isEmpty()) {

            // 파일이 제공되었고, 기존 이미지 URL이 존재하면 S3에서 기존 이미지 삭제
            if (existingUserDTO != null && existingUserDTO.profilePath() != null) {
                URL oldProfileUrl = new URL(existingUserDTO.profilePath());
                String olds3ProfilePath = oldProfileUrl.getPath().substring(1); // URL에서 객체 키 추출 (앞의 '/' 제거)
                log.info("olds3ProfilePath: " + olds3ProfilePath);
                s3Client.deleteObject(bucketName, olds3ProfilePath);
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = "profiles/" + userId + "_" + UUID.randomUUID().toString() + "." + extension;

            try {
                // S3에 프로필 이미지 업로드
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                String imageUrl = s3Client.getUrl(bucketName, fileName).toString();

                // 이미지 경로를 데이터베이스에 저장
                UserInfoDTO iDTO = UserInfoDTO.builder()
                        .userId(userId)
                        .profilePath(imageUrl)
                        .build();
                userInfoService.profilePathProc(iDTO);

                return ResponseEntity.ok("프로필 업데이트 성공!");
            } catch (Exception e) {
                log.info("error : " + e);
                return ResponseEntity.badRequest().body("프로필 등록에 실패했습니다.");
            }
        }

        return ResponseEntity.ok("프로필 업데이트 성공!");
    }

    // 회원 탈퇴 페이지
    @GetMapping(value = "withDraw")
    public String Withdrawal(HttpSession session, ModelMap model) {

        log.info(this.getClass().getName() + ".user/withdraw Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        model.addAttribute("userId", userId);

        if (userId != null) {
            log.info(this.getClass().getName() + ".user/withdraw End!");

            return "user/withDraw";
        } else {
            return "user/login";
        }
    }

    @ResponseBody
    @GetMapping(value = "withDrawProc")
    public ResponseEntity<?> withDrawProc(HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/withDrawProc Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        UserInfoDTO seqDTO = userInfoService.getUserSeq(userId);
        long userSeq = seqDTO.userSeq();

        log.info("userId : " + userId);
        log.info("userSeq : " + userSeq);

        UserInfoDTO userInfo = userInfoService.getUserInfo(userId);

        if (userInfo == null) {
            log.error("UserInfo not found for userId: " + userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        // Naver 계정일 경우
        if (userId.matches("naver_(.*)")) {

            log.info("네이버 로그인 계정 토큰 삭제 로직");
            log.info("userId : " + userId);
            log.info("password : " + userInfoService.getUserInfo(userId).password());

            naverService.deleteToken(
                    EncryptUtil.decAES128CBC(
                            userInfoService.getUserInfo(userId).password()
                    )
            );

            log.info("delete accessToken Success");

        }


        // 프로필 이미지 삭제 로직
        if (userInfo.profilePath() != null && !userInfo.profilePath().isEmpty()) {
            try {
                String fileName = userInfo.profilePath().substring(userInfo.profilePath().lastIndexOf("/") + 1);
                s3Client.deleteObject(bucketName, "profiles/" + fileName);
                log.info("S3 버킷에서 파일 삭제 성공: " + fileName);
            } catch (Exception e) {
                log.error("S3 버킷에서 파일 삭제 실패: " + e.getMessage());
            }
        }


        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userSeq(userSeq)
                .userId(userId)
                .userName(userInfo.userName())
                .nickName(userInfo.nickName())
                .build();

        log.info("pDTO : " + pDTO);

        // 회원탈퇴 로직 호출하여 결과 받기
        userInfoService.withDrawProc(pDTO);

        session.invalidate();

        log.info(this.getClass().getName() + ".user/withDrawProc End!");

        return ResponseEntity.ok().body("회원탈퇴 성공!");
    }
}