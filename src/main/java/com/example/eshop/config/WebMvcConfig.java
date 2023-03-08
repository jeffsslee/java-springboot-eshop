package com.example.eshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${uploadPath}") // uploadPath: "file:///D:/devspaces/intellij/demoProjects/eshopspace02/"
  String uploadPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/images/**")   // For every request of "/images/** "
        .addResourceLocations(uploadPath);  // Go and find resources in the location
  }
}
