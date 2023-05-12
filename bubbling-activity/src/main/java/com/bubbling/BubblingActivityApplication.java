package com.bubbling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication
@EnableTransactionManagement
public class BubblingActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BubblingActivityApplication.class, args);
    }
}
