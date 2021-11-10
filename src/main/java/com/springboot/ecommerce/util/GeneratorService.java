package com.springboot.ecommerce.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {
	public String get() {
		int length = 20;
		boolean useLetters = true;
		boolean useNumbers = true;
		String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
		return generatedString;
	}

	public String getRandomPassword(int passLength) {
		String generatedString = RandomStringUtils.random(passLength, false, true);
		return generatedString;
	}
}
