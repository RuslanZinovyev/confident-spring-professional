package com.myproject.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.ApplicationLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackageClasses = ApplicationLauncher.class)
@PropertySource("classpath:/application.properties")
@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MyProjectApplicationContext {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
