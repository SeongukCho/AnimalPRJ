package kopo.poly.service;

public interface IRecaptchaService {
    public boolean verifyRecaptcha(String recaptchaResponse);
}
