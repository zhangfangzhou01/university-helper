package com.yhm.universityhelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.yhm.universityhelper.dao")
public class UniversityhelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityhelperApplication.class, args);
    }

}
