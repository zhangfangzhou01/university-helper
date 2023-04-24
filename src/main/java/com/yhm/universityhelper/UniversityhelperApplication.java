package com.yhm.universityhelper;

import com.yhm.universityhelper.util.BeanUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.yhm.universityhelper.dao")
@EnableCaching
@EnableTransactionManagement
public class UniversityhelperApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext app = SpringApplication.run(UniversityhelperApplication.class, args);
        BeanUtils.setApplicationContext(app);
    }
}
