package com.springboot.ecommerce.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
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

import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.util.CloudService;

@Controller
@RequestMapping("/admin/accounts")
public class AAcountController {

	@Autowired
	private IAccountService service;

	@Autowired
	private CloudService cloud;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("")
	public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@RequestParam(value = "sort", required = false, defaultValue = "createAt") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "search", required = false, defaultValue = "") String search,
			@RequestParam(value = "role", required = false, defaultValue = "-1") Integer role, Model model) {
		Page<Account> response = service.paginated(page, limit, sort, order, role, search);
		this.setPaginated(response, page, limit, sort, order, role, search, model);
		System.out.println(BCrypt.hashpw("1233211", BCrypt.gensalt()));
		model.addAttribute("endPoint", "accounts");
		return "page/admin/account/list";
	}

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("account", new Account());
		return "page/admin/account/add";
	}

	@PostMapping("/add")
	public String handleAdd(@Valid @ModelAttribute("account") Account account, BindingResult result,
			@RequestPart("fileUpload") MultipartFile file, Model model) {
		try {
			if (!result.hasErrors()) {
				if (service.validateAccount(account)) {
					if (!file.isEmpty())
						account.setAvatar(cloud.uploadSingleFile(file));
					account = service.save(account);
					model.addAttribute("sucMess", "Successfully!");
				} else {
					model.addAttribute("errMess", "Email has already!");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("errMess", "Email has already!");

		}
		return "page/admin/account/add";
	}

	@GetMapping("/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Account account = service.findById(id);
		model.addAttribute("account", account);
		return "page/admin/account/edit";
	}

	@PostMapping("/edit")
	public String handleEdit(@Valid Account account, BindingResult result,
			@RequestPart("fileUpload") MultipartFile file, Model model) {
		try {
			System.out.println(account.toString());
			if (result.hasErrors()) {
				model.addAttribute("account", service.findById(account.getId()));
			} else {
				if (!file.isEmpty()) {
					account.setAvatar(cloud.uploadSingleFile(file));
				}
				account = service.save(account);
				model.addAttribute("sucMess", "Successfully!");

			}
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("errMess", "Failed!");

		}
		return "page/admin/account/edit";
	}

	@GetMapping("/delete/{id}")
	public String handleDelete(@PathVariable("id") Long id, Model model) {
		service.deleteById(id);
		return this.redirectPreviousPage();
	}
	
	@GetMapping("/{id}/orders")
	public String getOrders(@PathVariable("id") Long id, Model model) {
		Account account = service.findById(id);
		model.addAttribute("account", account);
		return "page/admin/account/orders";
	}

	private void setPaginated(Page<Account> response, Integer page, Integer limit, String sort, String order,
			Integer role, String search, Model model) {
		model.addAttribute("response", response);
		model.addAttribute("accounts", response.getContent());
		model.addAttribute("totalPages", response.getTotalPages());
		model.addAttribute("totalItems", response.getTotalElements());
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("sort", sort);
		model.addAttribute("order", order);
		model.addAttribute("reveser", order.equals("asc") || order.equals("") ? "desc" : "asc");
		model.addAttribute("role", role);
		model.addAttribute("search", search);
	}

	private String redirectPreviousPage() {
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}
}
