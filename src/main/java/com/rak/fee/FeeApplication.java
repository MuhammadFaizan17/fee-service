package com.rak.fee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeeApplication.class, args);
    }

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

}
