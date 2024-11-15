package com.osipalli.template.services.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@EnableFeignClients
@SpringBootApplication
public class IdentityApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityApplication.class, args);
    }

}
