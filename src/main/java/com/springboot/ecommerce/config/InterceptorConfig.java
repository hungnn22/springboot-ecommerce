package com.springboot.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.ecommerce.interceptor.AuthInterceptor;
import com.springboot.ecommerce.interceptor.LoggerInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private LoggerInterceptor loggerInterceptor;

	@Autowired
	private AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggerInterceptor).addPathPatterns("/carts/**", "/checkout", "/orders")
				.excludePathPatterns("/login", "/register", "/", "/reset", "/fotgot");

		registry.addInterceptor(authInterceptor).addPathPatterns("/admin/**");
	}
}
