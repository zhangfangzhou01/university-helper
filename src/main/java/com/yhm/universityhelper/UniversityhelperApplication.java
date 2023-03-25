package com.yhm.universityhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UniversityhelperApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniversityhelperApplication.class, args);
    }
}
