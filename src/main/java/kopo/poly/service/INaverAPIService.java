package kopo.poly.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import kopo.poly.config.OpenFeignConfig;
import kopo.poly.dto.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "naverAuthClient", url = "https://nid.naver.com", configuration = OpenFeignConfig.class)
public interface INaverAPIService {

    @RequestLine("POST /oauth2.0/token")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    TokenDTO getAccessToken(
            @Param("grant_type") String grantType,
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("redirect_uri") String redirectUri,
            @Param("code") String code
    );


    @RequestLine("POST /oauth2.0/token")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    TokenDTO deleteToken(
            @Param("grant_type") String grantType,
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("access_token") String accessToken,
            @Param("service_provider") String serviceProvider
    );
}