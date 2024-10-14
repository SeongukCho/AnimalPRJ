package kopo.poly.service.impl;

import kopo.poly.service.IRecaptchaService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RecaptchaService implements IRecaptchaService {
    private static final String secretKey = "6LdCQ2AqAAAAAF2vdP7QO_rSjkt60JKEZJtXy3Tc";
    private static final String requestUrl = "https://www.google.com/recaptcha/api/siteverify";

    // reCAPTCHA 응답 검증 메서드
    public boolean verifyRecaptcha(String recaptchaResponse) {
        try {
            URL url = new URL(requestUrl + "?secret=" + secretKey + "&response=" + recaptchaResponse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Google의 응답에서 "success" 값이 true 인지 확인
            return response.toString().contains("\"success\": true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
