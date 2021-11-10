package com.springboot.ecommerce.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.service.ICategoryService;

@Controller
@RequestMapping("/admin/categories")
public class ACategoryController {

	@Autowired
	private ICategoryService service;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("")
	public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@RequestParam(value = "sort", required = false, defaultValue = "name") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order,
			@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
		Page<Category> response = service.paginated(page, limit, sort, order, search);
		System.out.println("SIZE: " + response.getContent().size());
		this.setPaginated(response, page, limit, sort, order, search, model);
		model.addAttribute("endPoint", "categories");
		return "page/admin/category/list";
	}

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("category", new Category());
		return "page/admin/category/add";
	}

	@GetMapping("/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Category category = service.findById(id);
		model.addAttribute("category", category);
		return "page/admin/category/edit";
	}

	@PostMapping("/save")
	public String handleSave(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model) {
		if (!result.hasErrors()) {
			model.addAttribute("sucMess", "Successfully!");
			service.save(category);
		}
		return "page/admin/category/add";
	}

	@GetMapping("/delete/{id}")
	public String handleDelete(@PathVariable("id") Long id) {
		System.out.println(id);
		service.deleteById(id);
		return redirectPreviousPage();
	}

	private void setPaginated(Page<Category> response, Integer page, Integer limit, String sort, String order,
			String search, Model model) {
		model.addAttribute("response", response);
		model.addAttribute("categories", response.getContent());
		model.addAttribute("totalPages", response.getTotalPages());
		model.addAttribute("totalItems", response.getTotalElements());
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("sort", sort);
		model.addAttribute("order", order);
		model.addAttribute("reveser", order.equals("asc") || order.equals("") ? "desc" : "asc");
		model.addAttribute("search", search);
	}

	private String redirectPreviousPage() {
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}
}
