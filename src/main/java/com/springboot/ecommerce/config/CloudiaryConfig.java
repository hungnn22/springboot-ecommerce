package com.springboot.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springboot.ecommerce.constant.AppConstant;

@Configuration
public class CloudiaryConfig {
	@Bean
	public Cloudinary getInstance() {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", AppConstant.CLOUD_NAME, "api_key",
				AppConstant.API_KEY, "api_secret", AppConstant.API_SECRET, "secure", true));
		return cloudinary;
	}
}
