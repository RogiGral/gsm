package com.gymsystem.gms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

import static com.gymsystem.gms.constraints.FileConstant.USER_FOLDER;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class GmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(GmsApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
