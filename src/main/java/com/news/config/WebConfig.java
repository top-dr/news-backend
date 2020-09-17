package com.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class WebConfig {
	 
	@Bean
	public MappedInterceptor interceptor() {
	    return new MappedInterceptor(null, new ApiInterceptor());
	}

}
