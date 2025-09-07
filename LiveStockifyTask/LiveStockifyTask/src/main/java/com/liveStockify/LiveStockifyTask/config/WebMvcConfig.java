package com.liveStockify.LiveStockifyTask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files from /static/ directory
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
        
        // Serve CSS, JS, and other static resources
        registry.addResourceHandler("/styles.css", "/script.js", "/index.html")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
        
        // Serve all other static resources
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/index").setViewName("forward:/index.html");
    }
}
