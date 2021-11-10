package com.springboot.ecommerce.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Photo;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IPhotoService;
import com.springboot.ecommerce.service.IProductService;
import com.springboot.ecommerce.util.CloudService;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ICategoryService cService;

	@Autowired
	private CloudService cloud;

	@Autowired
	private IPhotoService photoService;

	@Override
	public Product findById(Long id) {
		return repository.getById(id);
	}

	@Override
	public List<Product> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Product> paginated(Integer page, Integer limit, String sort, String order, Long categoryId,
			String search) {
		Sort filters = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending()
				: Sort.by(sort).descending();
		Pageable pageable = PageRequest.of(page - 1, limit, filters);
		if (categoryId == -1 && search.equals("")) {
			return repository.findAll(pageable);
		} else if (categoryId != -1 && search.equals("")) {
			Category cate = cService.findById(categoryId);
			return repository.findByCategory(cate, pageable);
		} else if (categoryId == -1 && !search.equals("")) {
			return repository.findByNameContaining(search, pageable);
		} else {
			Category cate = cService.findById(categoryId);
			return repository.findByCategoryAndNameContaining(cate, search, pageable);
		}

	}

	@Override
	public List<Product> findTop6Popular() {
		return repository.findTop6Popular();
	}

	@Override
	public Product save(Product product, MultipartFile[] files) {
		product = repository.save(product);
		Photo photo = null;
		String url = "";
		System.out.println(files.length);
		if (files[0].getSize() > 0) {
			for (int i = 0; i < files.length; i++) {
				try {
					url = cloud.uploadSingleFile(files[0]);
					photo = photoService.save(new Photo(url, product));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return product;
	}

	@Override
	public Product save(Product product) {
		product = repository.save(product);
		return product;
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);

	}

	@Override
	public List<Product> findRelatedById(Long id) {
		return repository.findRelatedById(id);
	}

}
