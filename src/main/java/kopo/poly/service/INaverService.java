package kopo.poly.service;

import kopo.poly.dto.NaverDTO;
import kopo.poly.dto.TokenDTO;

public interface INaverService {

    /* 토큰 가져오기 */
    TokenDTO getAccessToken(String code) throws Exception;

    void deleteToken(String accessToken) throws Exception;

    /* 네이버에서 정보 가져오기 */
    NaverDTO getNaverUserInfo(TokenDTO pDTO) throws Exception;
}