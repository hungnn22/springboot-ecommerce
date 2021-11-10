package com.springboot.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {
	@Autowired
	private GeneratorService generateService;

	private final String BASE = System.getProperty("user.dir") + "/src/main/resources/static/upload";

	public String save(MultipartFile file) throws IllegalStateException, IOException {
		String typeFile = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);

		File saveFile = new File(BASE, file.getOriginalFilename());

		file.transferTo(saveFile);
		return saveFile.getName();
	}

	public String saveFile(MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(BASE);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(multipartFile.getOriginalFilename());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			return multipartFile.getOriginalFilename();
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: ");
		}
	}
}
