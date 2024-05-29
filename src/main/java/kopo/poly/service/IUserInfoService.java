package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;
    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    // 아이디 정보 가져오기
    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    int getUserLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO sendSignUpEmailAuth(UserInfoDTO pDTO) throws Exception;

    // 아이디, 비밀번호 찾기에 활용
    int searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception;

    int updatePassword(UserInfoDTO pDTO) throws Exception;

    int updateUserInfo(UserInfoDTO pDTO) throws Exception;
    void deleteUserInfo (UserInfoDTO pDTO) throws Exception;
}