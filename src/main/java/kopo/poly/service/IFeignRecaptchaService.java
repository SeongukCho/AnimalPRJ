package kopo.poly.service;

import feign.RequestLine;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(name = "recaptchaClient", url = "https://www.google.com/recaptcha/api")
public interface IFeignRecaptchaService {
    @RequestLine("POST /siteverify")@Headers("Content-Type: application/x-www-form-urlencoded")
    Map<String, Object> verifyRecaptcha(@Param("secret") String secret,
                                        @Param("response") String response);
}