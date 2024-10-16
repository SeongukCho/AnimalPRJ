package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    // 아이디 정보 가져오기
    UserInfoDTO getUserInfo(String userId) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    int insertSocialUser(String userId,
                         String password,
                         String email,
                         String nickname,
                         String userName) throws Exception;

    int getUserLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO sendSignUpEmailAuth(UserInfoDTO pDTO) throws Exception;

    // 아이디, 비밀번호 찾기에 활용
    int searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserSeq(String userId) throws Exception;

    UserInfoDTO getNickName(String userId) throws Exception;

    String getUserIdExist(String userId) throws Exception;

    /**
     * 비밀번호 변경
     *
     * @param pDTO 회원정보
     */
    int updatePassword(UserInfoDTO pDTO) throws Exception;

    /**
     * 닉네임 변경
     *
     * @param pDTO 회원정보
     */
    void newNickNameProc(UserInfoDTO pDTO) throws Exception;

    int updateUserInfo(UserInfoDTO pDTO) throws Exception;

    /**
     * 프로필 사진 등록
     *
     * @param pDTO 회원정보
     */
    void profilePathProc(UserInfoDTO pDTO) throws Exception;

    /**
     * 회원탈퇴
     *
     * @param pDTO 회원정보
     */
    void withDrawProc(UserInfoDTO pDTO) throws Exception;
}