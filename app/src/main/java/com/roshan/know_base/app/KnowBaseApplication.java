package com.roshan.know_base.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.roshan.know_base")
public class KnowBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowBaseApplication.class, args);
    }
}
