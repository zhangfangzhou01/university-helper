package com.yhm.universityhelper;

import com.yhm.universityhelper.util.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UniversityhelperApplication {
    public static void main(String[] args) {
        BeanUtils.setApplicationContext(SpringApplication.run(UniversityhelperApplication.class, args));
    }
}
