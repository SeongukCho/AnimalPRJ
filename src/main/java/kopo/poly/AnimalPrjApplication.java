package kopo.poly;

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
