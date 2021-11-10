package com.springboot.ecommerce.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.springboot.ecommerce.entity.Category;

public interface ICategoryService {

	List<Category> getAll();

	Category findById(Long id);

	Page<Category> paginated(Integer page, Integer limit, String sort, String order, String search);

	void save(Category category);

	void deleteById(Long id);

}
