package com.springboot.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboot.ecommerce.dto.ProductSoldDTO;
import com.springboot.ecommerce.entity.OrderDetail;

public interface IOrderDetailService {

	void addToCart(Long productId);

	List<OrderDetail> findByAccountId();

	Double getCartTotals();

	void update(Long id, Integer qty);

	Integer getCartItems();

	void remove(Long id);

	void removeAll();

	List<Object[]> getRevenue(Long statusId, Integer year, Integer page, Integer limit);

	List<Object[]> getProductSoldReport(String time, Integer page, Integer limit);

}
