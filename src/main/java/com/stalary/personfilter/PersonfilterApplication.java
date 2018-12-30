package com.stalary.personfilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PersonfilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonfilterApplication.class, args);
    }
}
