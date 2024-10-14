package kopo.poly.controller;

import kopo.poly.service.impl.RecaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/recaptcha")
@RequiredArgsConstructor
@Slf4j
public class RecaptchaController {
    private final RecaptchaService recaptchaService;

    @ResponseBody
    @PostMapping("/verify-recaptcha")public ResponseEntity<String> verifyRecaptcha(@RequestBody Map<String, String> request) {
        String token= request.get("token");

        boolean isValid= recaptchaService.verifyRecaptcha(token);
        log.info("isValid" + isValid);

        if (isValid) {
            return ResponseEntity.ok("Verification successful");
        } else {
            return ResponseEntity.badRequest().body("Verification failed");
        }
    }
}