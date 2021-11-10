package com.springboot.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.dto.CheckoutDTO;
import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IOrderService;

@Controller
public class CartController {

	@Autowired
	private IOrderDetailService orderDetailService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ICategoryService cService;

	@Autowired
	private IOrderService orderService;

	@GetMapping("/carts")
	public String cartList(Model model) {

		return "page/site/cart/cart";
	}

	@GetMapping("/carts/add/{productID}")
	public String add(@PathVariable("productID") Long productId) {
		orderDetailService.addToCart(productId);
		System.out.println(productId);
		return redirectPreviousPage();
	}

	@GetMapping("/carts/remove/{id}")
	public String remove(@PathVariable("id") Long id) {
		orderDetailService.remove(id);
		return redirectPreviousPage();
	}

	@GetMapping("/carts/remove/all")
	public String removeAll() {
		orderDetailService.removeAll();
		return "redirect:/carts";
	}

	@GetMapping("/carts/update/{id}")
	public String udpate(@PathVariable("id") Long id, @RequestParam("qty") Integer qty, Model model) {
		orderDetailService.update(id, qty);
		return "redirect:/carts";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("checkout", new CheckoutDTO());
		return "page/site/cart/checkout";
	}

	@PostMapping("/checkout")
	public String handleCheckout(@Valid @ModelAttribute("checkout") CheckoutDTO checkout, BindingResult result) {
		if (result.hasErrors()) {
			return "page/site/cart/checkout";
		}
		orderService.checkout(checkout);
		return "redirect:/carts";
	}

	private String redirectPreviousPage() {
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
