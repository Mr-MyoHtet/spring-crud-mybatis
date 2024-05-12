package com.mybatiscrud.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class StaticResourceConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Path to external directory
		String externalDirectoryPath = "src/main/resources/static/images/";

		// Resource handler for serving files directly from an external directory
		registry.addResourceHandler("/images/**").addResourceLocations(externalDirectoryPath);
	}

}
