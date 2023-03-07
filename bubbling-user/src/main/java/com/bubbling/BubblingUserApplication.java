package com.bubbling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class BubblingUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BubblingUserApplication.class, args);
    }

}
