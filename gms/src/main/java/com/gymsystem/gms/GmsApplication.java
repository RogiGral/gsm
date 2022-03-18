package com.gymsystem.gms;

import com.gymsystem.gms.configuration.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

import static com.gymsystem.gms.constraints.FileConstant.USER_FOLDER;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Import(SecurityConfiguration.class)
@EnableWebSecurity
public class GmsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(GmsApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
