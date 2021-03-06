package com.springboot.ecommerce.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.springboot.ecommerce.config.CloudiaryConfig;

@Service
public class CloudService {

	@Autowired
	private CloudiaryConfig cloud;

	public String uploadSingleFile(MultipartFile file) throws IOException {
		Map result = cloud.getInstance().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return (String) result.get("url");
	}
}
