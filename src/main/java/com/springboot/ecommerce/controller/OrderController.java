package com.springboot.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IOrderService;
import com.springboot.ecommerce.util.SessionService;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private IOrderDetailService orderDetailService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ICategoryService cService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private SessionService session;

	@GetMapping("")
	public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
		try {
			Account account = session.get("account");
			Page<Order> response = orderService.paginated(page, account.getId());
			this.setPaginated(response, page, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "page/site/cart/history";
	}

	private void setPaginated(Page<Order> response, Integer page, Model model) {
		model.addAttribute("response", response);
		model.addAttribute("orders", response.getContent());
		model.addAttribute("totalPages", response.getTotalPages());
		model.addAttribute("totalItems", response.getTotalElements());
		model.addAttribute("page", page);
	}

	@GetMapping("/update/reject")
	public String handleReject(@RequestParam("orderId") Long orderId) {
		orderService.reject(orderId);
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}

	@GetMapping("/update/confirm")
	public String handleConfirm(@RequestParam("orderId") Long orderId) {
		orderService.confirm(orderId);
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}

	@ModelAttribute("orderDetails")
	public List<OrderDetail> getOrderDetails() {
		return orderDetailService.findByAccountId();
	}

	@ModelAttribute("cartTotals")
	public Double getCartTotals() {
		return orderDetailService.getCartTotals();
	}

	@ModelAttribute("cartItems")
	public Integer getCartItems() {
		return orderDetailService.getCartItems();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return cService.getAll();
	}
}
