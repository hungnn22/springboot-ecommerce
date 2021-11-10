package com.springboot.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IProductService;

@Controller
public class HomeController {

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IOrderDetailService orderDetailService;
	
	@Autowired
	private HttpServletRequest request;

	@GetMapping("/")
	public String index(@RequestParam(value = "od", required = false) List<OrderDetail> orderDetails, Model model) {

		Page<Product> response = productService.paginated(1, 8, "createAt", "asc", -1L, "");
		List<Product> products = response.getContent();

		model.addAttribute("products", products);
		model.addAttribute("od", orderDetails);

		return "page/site/home";

	}

	@GetMapping("/back")
	public String back() {
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		System.out.println(referrer);
		return "redirect:" + referrer;
	}

	@ModelAttribute("populars")
	public List<Product> getPopulars() {
		return productService.findTop6Popular();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryService.getAll();
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
}
