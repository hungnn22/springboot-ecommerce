package com.springboot.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.OrderDetail;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IProductService;

import lombok.val;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ICategoryService cService;

	@Autowired
	private IProductService pService;
	@Autowired
	private IOrderDetailService orderDetailService;

	@GetMapping(value = "", params = "id")
	public String detail(@RequestParam("id") Long id, Model model) {
		Product product = pService.findById(id);
		List<Product> relatived = pService.findRelatedById(id);
		model.addAttribute("product", product);
		model.addAttribute("relatived", relatived);
		return "page/site/product/detail";
	}

	@GetMapping(value = { "", "/" })
	public String paginated(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "4") Integer limit,
			@RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order,
			@RequestParam(value = "categoryId", required = false, defaultValue = "-1") Long categoryId,
			@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {

		Page<Product> response = pService.paginated(page, limit, sort, order, categoryId, search);

		this.setPaginated(response, page, limit, sort, order, categoryId, search, model);

		return "page/site/product/search";

	}

	private void setPaginated(Page<Product> response, Integer page, Integer limit, String sort, String order,
			Long categoryId, String search, Model model) {
		model.addAttribute("response", response);
		model.addAttribute("products", response.getContent());
		model.addAttribute("totalPages", response.getTotalPages());
		model.addAttribute("totalItems", response.getTotalElements());
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("sort", sort);
		model.addAttribute("order", order);
		model.addAttribute("reveserOrder", order.equals("asc") ? "desc" : "asc");
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("search", search);
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
