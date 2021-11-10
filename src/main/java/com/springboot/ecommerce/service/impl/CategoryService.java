package com.springboot.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository repository;

	@Override
	public List<Category> getAll() {
		return repository.findAll();
	}

	@Override
	public Category findById(Long id) {
		return repository.getById(id);
	}

	@Override
	public Page<Category> paginated(Integer page, Integer limit, String sort, String order, String search) {
		Sort filters = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();
		Pageable pageable = PageRequest.of(page - 1, limit, filters);
		return repository.findByNameContaining(search, pageable);
	}

	@Override
	public void save(Category category) {
		repository.save(category);

	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
