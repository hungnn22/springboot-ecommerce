package com.springboot.ecommerce.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.springboot.ecommerce.dto.CheckoutDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Order;

public interface IOrderService {

	Order findOrderNotConfirmedByAccountId(Long id);

	Order save(Order detail);

	void checkout(CheckoutDTO checkout);

	List<Order> findByAccount(Account account);

	void reject(Long orderId);

	List<Order> findAll();

	List<Order> findTop4();

	void confirm(Long orderId);

	Page<Order> paginated(Integer page, Integer limit, String sort, String order, Long accountId, Long statusId,
			String search);

	Page<Order> paginated(Integer page, Long accountId);

	Long getPendingCount();

}
