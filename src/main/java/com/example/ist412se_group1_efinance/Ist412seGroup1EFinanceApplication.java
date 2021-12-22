package com.example.ist412se_group1_efinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// To exclude security auto configuration: @SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Ist412seGroup1EFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ist412seGroup1EFinanceApplication.class, args);
    }
}

