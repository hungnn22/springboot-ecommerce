package com.springboot.ecommerce.service;

import com.springboot.ecommerce.entity.Photo;

public interface IPhotoService {

	Photo save(Photo photo);

	void deleteById(Long id);

}
