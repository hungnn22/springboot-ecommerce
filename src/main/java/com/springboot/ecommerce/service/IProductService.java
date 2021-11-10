package com.springboot.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ecommerce.entity.Product;

public interface IProductService {

	Product findById(Long id);

	Page<Product> paginated(Integer page, Integer limit, String sort, String order, Long categoryId, String search);

	List<Product> findAll();

	List<Product> findTop6Popular();

	Product save(Product product, MultipartFile[] files);

	void deleteById(Long id);

	List<Product> findRelatedById(Long id);

	Product save(Product product);

}
