package com.yhm.universityhelper;

import com.yhm.universityhelper.util.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UniversityhelperApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext app = SpringApplication.run(UniversityhelperApplication.class, args);
        BeanUtils.setApplicationContext(app);
    }
}
