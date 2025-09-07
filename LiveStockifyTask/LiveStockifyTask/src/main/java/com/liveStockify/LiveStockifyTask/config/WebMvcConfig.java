package com.liveStockify.LiveStockifyTask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
   
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
        
        registry.addResourceHandler("/styles.css", "/script.js", "/index.html")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
        
   
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/index").setViewName("forward:/index.html");
    }
}

