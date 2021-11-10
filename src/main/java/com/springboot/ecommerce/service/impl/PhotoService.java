package com.springboot.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Photo;
import com.springboot.ecommerce.repository.PhotoRepository;
import com.springboot.ecommerce.service.IPhotoService;

@Service
public class PhotoService implements IPhotoService {

	@Autowired
	private PhotoRepository repository;

	@Override
	public Photo save(Photo photo) {
		return repository.save(photo);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
