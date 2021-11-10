package com.springboot.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("FROM Product p WHERE p.qty > 0")
	Page<Product> findAllSite(Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable pageable);

	Page<Product> findByNameContaining(String name, Pageable pageable);

	Page<Product> findByCategoryAndNameContaining(Category category, String name, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT c.* FROM orders a JOIN order_details b on a.id = b.order_id JOIN products c on b.product_id = c.id GROUP BY c.name ORDER BY SUM(b.qty) DESC LIMIT 6")
	List<Product> findTop6Popular();

	@Query(nativeQuery = true, value = "SELECT DISTINCT a.* FROM products a \r\n"
			+ "JOIN order_details b on a.id = b.product_id\r\n" + "JOIN orders c on b.order_id = c.id\r\n"
			+ "WHERE c.id in (\r\n" + "    SELECT d.id FROM orders d \r\n"
			+ "	JOIN order_details e on d.id = e.order_id\r\n" + "	WHERE e.product_id = ?1\r\n" + "	)\r\n"
			+ "    AND a.id != ?1\r\n" + "\r\n" + "")
	List<Product> findRelatedById(Long id);

}
