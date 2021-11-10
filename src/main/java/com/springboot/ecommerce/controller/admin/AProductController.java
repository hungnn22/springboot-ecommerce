package com.springboot.ecommerce.controller.admin;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Color;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.entity.Size;
import com.springboot.ecommerce.service.ICategoryService;
import com.springboot.ecommerce.service.IColorService;
import com.springboot.ecommerce.service.IProductService;
import com.springboot.ecommerce.service.ISizeService;

@Controller
@RequestMapping("/admin/products")
public class AProductController {

	@Autowired
	private IProductService service;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private IColorService colorService;

	@Autowired
	private ISizeService sizeService;

	@GetMapping(value = { "/", "" })
	public String paginated(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@RequestParam(value = "sort", required = false, defaultValue = "createAt") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "categoryId", required = false, defaultValue = "-1") Long categoryId,
			@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {

		Page<Product> response = service.paginated(page, limit, sort, order, categoryId, search);
		this.setPaginated(response, page, limit, sort, order, categoryId, search, model);
		model.addAttribute("endPoint", "products");
		return "page/admin/product/product";
	}

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("product", new Product());
		return "page/admin/product/add";
	}

	@PostMapping("/add")
	public String handleAdd(@Valid @ModelAttribute("product") Product product, BindingResult result,
			@RequestPart("image_uploads") MultipartFile[] files, Model model) {
		if (result.hasErrors() || files[0].getSize() == 0) {
			model.addAttribute("errMess", "Chọn ảnh đê!");
			return "page/admin/product/add";
		} else {
			System.out.println(product.toString());
			service.save(product, files);
			model.addAttribute("sucMess", "Successfully!");
			return "page/admin/product/add";
		}

	}

	@GetMapping("/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Product product = service.findById(id);
		model.addAttribute("product", product);
		return "page/admin/product/edit";
	}

	@PostMapping("/edit")
	public String handleEdit(@Valid Product product, BindingResult result,
			@RequestPart("image_uploads") MultipartFile[] files, Model model) {
		if (result.hasErrors()) {
			product.setPhotos(service.findById(product.getId()).getPhotos());
		} else {
			System.out.println(product.toString());
			product = service.save(product, files);
			model.addAttribute("sucMess", "Successfully!");
		}
		model.addAttribute("product", product);
		return "page/admin/product/edit";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		service.deleteById(id);
		return redirectPreviousPage();
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
		model.addAttribute("reveser", order.equals("asc") || order.equals("") ? "desc" : "asc");
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("search", search);
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryService.getAll();
	}

	@ModelAttribute("colors")
	public List<Color> getColors() {
		return colorService.findAll();
	}

	@ModelAttribute("sizes")
	public List<Size> getSizes() {
		return sizeService.findAll();
	}

	private String redirectPreviousPage() {
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}
}
