package com.mybatiscrud.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
	
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        // Disable caching for all static resources
	        registry.addResourceHandler("/**")
	                .addResourceLocations("classpath:/static/images")
	                .setCachePeriod(0); // Set cache period to 0 seconds to disable caching
	    }

}
