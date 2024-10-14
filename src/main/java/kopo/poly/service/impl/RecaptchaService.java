package kopo.poly.service.impl;

import kopo.poly.service.IFeignRecaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecaptchaService {
    private final IFeignRecaptchaService recaptchaService;

    @Value("${v3.secretKey}")
    private String secretKey;

    public boolean verifyRecaptcha(String token) {
        Map<String, Object> response = recaptchaService.verifyRecaptcha(secretKey, token);

        log.info("response : " + response);

        boolean success = (boolean) response.get("success");
        double score = (double) response.get("score");
        return success && score > 0.5;
    }
}