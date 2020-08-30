package com.news;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class WebConfig {
	 
	@Bean
	public MappedInterceptor someMethodName() {
	    return new MappedInterceptor(null, new ApiInterceptor());
	}

}
