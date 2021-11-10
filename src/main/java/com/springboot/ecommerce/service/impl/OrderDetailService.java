package com.springboot.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.repository.OrderDetailRepository;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IOrderService;
import com.springboot.ecommerce.service.IProductService;
import com.springboot.ecommerce.util.SessionService;

@Service
@SessionScope
public class OrderDetailService implements IOrderDetailService {

	@Autowired
	private OrderDetailRepository repository;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private SessionService session;

	@Autowired
	private IProductService productService;

	@Override
	public void addToCart(Long productId) {
		Order order = null;
		OrderDetail orderDetail = null;
		System.out.println(productId);
		Account account = session.get("account");
		if (account != null) {
			Product product = productService.findById(productId);
			order = orderService.findOrderNotConfirmedByAccountId(account.getId());

			if (order == null) {
				order = orderService.save(new Order(account));
			}
			try {
				orderDetail = repository.findByOrder(order).stream()
						.filter((item) -> item.getProduct().getId().equals(product.getId()))
						.collect(Collectors.toList()).get(0);
				orderDetail.setQty(orderDetail.getQty() + 1);
			} catch (Exception e) {
				orderDetail = new OrderDetail(product.getPrice(), order, product);
			}
			repository.save(orderDetail);
		}
	}

	@Override
	public List<OrderDetail> findByAccountId() {
		try {
			Account account = session.get("account");
			return repository.findByAccountId(account.getId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Double getCartTotals() {
		List<OrderDetail> details = findByAccountId();
		if (details == null)
			return 0.0;
		Double cartTotals = details.stream().mapToDouble(item -> item.getPrice() * item.getQty()).reduce(0,
				(a, b) -> a + b);
		return cartTotals;
	}

	@Override
	public Integer getCartItems() {
		List<OrderDetail> details = findByAccountId();
		if (details == null)
			return 0;
		Integer count = details.stream().mapToInt(item -> item.getQty()).reduce(0, (a, b) -> a + b);
		return count;
	}

	@Override
	public void update(Long id, Integer qty) {
		if (qty == 0) {
			repository.deleteById(id);
		} else {
			OrderDetail orderDetail = repository.getById(id);
			orderDetail.setQty(qty);
			repository.save(orderDetail);
		}

	}

	@Override
	public void remove(Long id) {
		repository.deleteById(id);

	}

	@Override
	public void removeAll() {
		try {
			List<OrderDetail> details = this.findByAccountId();
			details.forEach(item -> {
				repository.deleteById(item.getId());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Object[]> getProductSoldReport(String time, Integer page, Integer limit) {
		List<Object[]> reports = null;
		int offset = (page - 1) * limit;
		switch (time) {
		case "week":
			reports = repository.getProductSoldReportByWeek(limit, offset);
			break;
		case "month":
			reports = repository.getProductSoldReportByMonth(limit, offset);
			break;
		case "year":
			reports = repository.getProductSoldReportByYear(limit, offset);
			break;
		default:
			reports = repository.getProductSoldReportToday(limit, offset);
			break;
		}
		return reports;
	}

	@Override
	public List<Object[]> getRevenue(Long statusId, Integer year, Integer page, Integer limit) {
		List<Object[]> reports = null;
		int offset = (page - 1) * limit;
		reports = repository.getRevenue(statusId, year, limit, offset);
		return reports;
	}
}
