package com.example.audiva.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mp3/**")
                .addResourceLocations("file:uploads/mp3/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/picture/**")
                .addResourceLocations("file:uploads/picture/")
                .setCachePeriod(3600);
    }
}
