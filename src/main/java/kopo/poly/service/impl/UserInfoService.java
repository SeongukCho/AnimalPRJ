package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final IMailService mailService; // 메일 발송을 위한 MailService 자바 객체 가져오기

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        UserInfoDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getNickNameExists Start!");

        UserInfoDTO rDTO;

        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("nickName : " + nickName);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByNickName(nickName);

        if (rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getNickNameExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getUserInfo(String userId) throws Exception {

        log.info(this.getClass().getName() + ".getUserInfo Start!");

        UserInfoDTO rDTO;

        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {

            String userName = rEntity.get().getUserName();
            String nickName = rEntity.get().getNickName();
            String email = EncryptUtil.decAES128CBC(rEntity.get().getEmail());
            String regDt = rEntity.get().getRegDt();
            String profilePath = rEntity.get().getProfilePath();

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("nickName : " + nickName);
            log.info("email : " + email);
            log.info("regDt : " + regDt);

            rDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .userName(userName)
                    .nickName(nickName)
                    .email(email)
                    .regDt(regDt)
                    .existsYn("Y")
                    .profilePath(profilePath)
                    .build();

        } else {

            rDTO = UserInfoDTO.builder().existsYn("N").build();

        }

        log.info(this.getClass().getName() + ".getUserInfo End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        // 회원가입 설공 : 1, 아이디 중복으로 인한 가입 취소 : 2, 기타 에러 발생 : 0
        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String nickName = CmmUtil.nvl(pDTO.nickName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());

        log.info("pDTO : " + pDTO);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            res = 2;
        } else {

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId)
                    .userName(userName)
                    .nickName(nickName)
                    .password(password)
                    .email(email)
                    .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            // 회원가입 여부 확인
            rEntity = userInfoRepository.findByUserId(userId);

            if (rEntity.isPresent()) { // 값이 있으면 가입 성공
                res = 1;
            } else { // 값이 없다면
                res = 0;
            }

        }

        log.info((this.getClass().getName() + ".insertUserInfo End!"));

        return res;
    }

    @Override
    public int getUserLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserLogin Start!");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String password = CmmUtil.nvl(pDTO.password());

        log.info("userId : " + userId);
        log.info("password : " + password);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndPassword(userId, password);

        if (rEntity.isPresent()) {
            res = 1;
        }

        log.info(this.getClass().getName() + ".getUserLoginCheck End!");

        return res;
    }

    /**
     * 회원가입 이메일 인증 보내기
     **/
    @Override
    public UserInfoDTO sendSignUpEmailAuth(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".sendSignUpEmailAuth Start!");

        String existsYn = pDTO.existsYn();
        ;


        int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

        log.info("authNumber : " + authNumber);

        // 인증번호 발송 로직
        MailDTO dto = MailDTO.builder()
                .title("비밀번호 변경 이메일 확인 인증번호 발송 메일")
                .contents("인증번호는 " + authNumber + "입니다.")
                .toMail(EncryptUtil.decAES128CBC(pDTO.email()))
                .build();

        log.info("dto Title : " + dto.title());
        log.info("dto contents : " + dto.contents());
        log.info("dto toMail : " + dto.toMail());

        mailService.doSendMail(dto); // 메일 발송

        //메일 변수값 초기화
        dto = null;

        pDTO = UserInfoDTO.builder()
                .authNumber(authNumber)
                .existsYn(existsYn)
                .build(); // 인증번호를 결과값에 넣어주기


        log.info(this.getClass().getName() + ".sendSignUpEmailAuth End!");

        return pDTO;
    }

    /**
     * 비밀번호 찾기, 이메일 변경 이메일 인증 보내기
     **/
    @Override
    public UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과 존재
        UserInfoDTO rDTO = getEmailExists(pDTO);
        log.info("rDTO : " + rDTO);

        String existsYn = CmmUtil.nvl(rDTO.existsYn());
        log.info("existsYn : " + existsYn);

        if (existsYn.equals("Y")) {
            // 6자리 랜덤 숫자 생성하기

            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = MailDTO.builder()
                    .title("비밀번호 변경 이메일 확인 인증번호 발송 메일")
                    .contents("인증번호는 " + authNumber + "입니다.")
                    .toMail(EncryptUtil.decAES128CBC(pDTO.email()))
                    .build();

            log.info("dto Title : " + dto.title());
            log.info("dto contents : " + dto.contents());
            log.info("dto toMail : " + dto.toMail());

            mailService.doSendMail(dto); // 메일 발송

            //메일 변수값 초기화
            dto = null;

            rDTO = UserInfoDTO.builder()
                    .authNumber(authNumber)
                    .existsYn(existsYn)
                    .build(); // 인증번호를 결과값에 넣어주기
        }

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getEmailExists Start!");

        UserInfoDTO rDTO;

        String email = CmmUtil.nvl(pDTO.email());

        log.info("email : " + email);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByEmail(email);

        if (rEntity.isPresent()) {

            UserInfoEntity userInfoEntity = rEntity.get();
            String userId = userInfoEntity.getUserId();
            log.info("userId : " + userId);

            rDTO = UserInfoDTO.builder()
                    .existsYn("Y")
                    .userId(userId)
                    .build();
        } else {

            // 6자리 랜덤 숫자 생성하기
            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = MailDTO.builder()
                    .title("이메일 중복 확인 인증번호 발송 메일")
                    .contents("인증번호는 " + authNumber + " 입니다.")
                    .toMail(EncryptUtil.decAES128CBC(email))
                    .build();

            mailService.doSendMail(dto); // 이메일 발송

            rDTO = UserInfoDTO.builder()
                    .existsYn("N")
                    .authNumber(authNumber)
                    .build();// 인증번호를 결과값에 넣어주기
        }

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @Override
    public int searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc Start!");

        String userId = pDTO.userId();
        String email = pDTO.email();

        log.info("userId : " + userId);
        log.info("email : " + email);

        int res = 0;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndEmail(userId, email);

        if (rEntity.isPresent()) {
            res = 1;
        }

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc End!");

        return res;

    }

    /**
     * 닉네임 변경
     *
     * @param pDTO 회원정보
     */
    @Transactional
    @Override   // 닉네임 변경 함수
    public void newNickNameProc(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "newNickNameProc start!");

        log.info("nickName : " + pDTO.nickName());

        String userId = CmmUtil.nvl(pDTO.userId());

        Optional<UserInfoEntity> uEntity = userInfoRepository.findByUserId(userId);

        if (uEntity.isPresent()) {

            UserInfoEntity rEntity = uEntity.get();

            log.info("userName : " + rEntity.getUserName());

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(rEntity.getUserId())
                    .userName(rEntity.getUserName())
                    .nickName(pDTO.nickName())
                    .password(rEntity.getPassword())
                    .email(rEntity.getEmail())
                    .regDt(rEntity.getRegDt())
                    .profilePath(rEntity.getProfilePath())
                    .build();

            userInfoRepository.save(pEntity);
        }

        log.info(this.getClass().getName() + "newNickNameProc end!");
    }

    @Override
    public int updatePassword(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updatePassword Start!");

        String userId = pDTO.userId();
        String password = pDTO.password();
        int res = 0;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {

            String userName = rEntity.get().getUserName();
            String nickName = rEntity.get().getNickName();
            String email = rEntity.get().getEmail();
            String regDt = rEntity.get().getRegDt();

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("email : " + email);
            log.info("regDt : " + regDt);

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId)
                    .userName(userName)
                    .nickName(nickName)
                    .password(password)
                    .email(email)
                    .regDt(regDt)
                    .build();

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            res = 1;
        }

        log.info(this.getClass().getName() + ".updatePassword END!");

        return res;

    }

    @Override
    public int updateUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        String userId = pDTO.userId();

        int res = 0;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {

            String nickName = pDTO.nickName();

            log.info("userId : " + userId);
            log.info("nickName : " + nickName);

            // 회원정보 DB에 저장
            userInfoRepository.updateUserInfo(userId, nickName);

            res = 1;

            log.info("res : " + res);

        }

        log.info(this.getClass().getName() + ".updateUserInfo END!");

        return res;

    }

    @Override
    public void deleteUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteUserInfo Start!");

        String userId = pDTO.userId();

        log.info("userId : " + userId);


        // 데이터 수정하기
        userInfoRepository.deleteById(userId);

        log.info(this.getClass().getName() + ".deleteUserInfo End!");

    }

    @Transactional
    @Override
    public void profilePathProc(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "profilePathProc start!");

        String userId = CmmUtil.nvl(pDTO.userId());

        // 사용자 ID로 사용자 엔티티 조회
        Optional<UserInfoEntity> uEntity = userInfoRepository.findByUserId(userId);

        if (uEntity.isPresent()) {

            UserInfoEntity rEntity = uEntity.get();

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(rEntity.getUserId())
                    .userName(rEntity.getUserName())
                    .nickName(rEntity.getNickName())
                    .password(rEntity.getPassword())
                    .email(rEntity.getEmail())
                    .regDt(rEntity.getRegDt())
                    .profilePath(pDTO.profilePath())
                    .build();

            userInfoRepository.save(pEntity);

        } else {
            log.error("No user found with ID: " + userId);
            throw new RuntimeException("User not found");
        }

        log.info(this.getClass().getName() + "profilePathProc End!");

    }
}