package com.springboot.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ParamService {

	@Autowired
	HttpServletRequest request;
	
	public String getString(String name, String defaultValue) {
		return request.getParameter(name) != null ? request.getParameter(name) : defaultValue; 
	}
	
	public int getInt(String name, int defaultValue){
		return request.getParameter(name) != null ? Integer.parseInt(request.getParameter(name)) : defaultValue; 
	}

	public double getDouble(String name, double defaultValue){
		return request.getParameter(name) != null ? Double.parseDouble(request.getParameter(name)) : defaultValue; 
	}

	public boolean getBoolean(String name, boolean defaultValue){
		return request.getParameter(name) != null ? Boolean.parseBoolean(request.getParameter(name)) : defaultValue; 
	}

	public Date getDate(String name, String pattern) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return request.getParameter(name) != null ? format.parse(request.getParameter(name)) : null;

	}

	public File save(MultipartFile file, String path) throws IllegalStateException, IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File saveFile = new File(dir, file.getOriginalFilename());
		file.transferTo(saveFile);
		return saveFile;
	}
}
