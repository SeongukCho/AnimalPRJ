package kopo.poly;

import kopo.poly.service.impl.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnimalPrjApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnimalPrjApplication.class, args);
    }


}
