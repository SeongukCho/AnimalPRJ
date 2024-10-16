package kopo.poly.service.impl;

import kopo.poly.dto.NaverDTO;
import kopo.poly.dto.TokenDTO;
import kopo.poly.service.INaverAPILoginService;
import kopo.poly.service.INaverAPIService;
import kopo.poly.service.INaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverService implements INaverService {

    @Value("${naver.client.id}")
    private String naverClientId;
    @Value("${naver.client.secret}")
    private String naverClientSecret;
    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

    private final INaverAPIService naverAPIService;
    private final INaverAPILoginService naverAPILoginService;

    /* 토큰 가져오기 */
    @Override
    public TokenDTO getAccessToken(String code) throws Exception {

        log.info("accessToken 얻기");

        return naverAPIService.getAccessToken(
                "authorization_code",
                naverClientId,
                naverClientSecret,
                naverRedirectUri,
                code
        );
    }

    /* 네이버에서 정보 가져오기 */
    @Override
    public NaverDTO getNaverUserInfo(TokenDTO pDTO) throws Exception {

        log.info("service getNaverUserInfo");

        return naverAPILoginService.getUserInfo(pDTO.access_token());
    }

    @Override
    public void deleteToken(String accessToken) throws Exception {

        log.info("accessToken 삭제");

        naverAPIService.deleteToken(
                "delete",
                naverClientId,
                naverClientSecret,
                accessToken,
                "NAVER"
        );
    }
}