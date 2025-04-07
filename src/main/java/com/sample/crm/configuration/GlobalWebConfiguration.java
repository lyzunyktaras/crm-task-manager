package com.sample.crm.configuration;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalWebConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");

                registry.addMapping("/v3/api-docs")
                    .allowedOrigins("*")
                    .allowedMethods("GET");

                registry.addMapping("/swagger-ui/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET");
            }
        };
    }
}
