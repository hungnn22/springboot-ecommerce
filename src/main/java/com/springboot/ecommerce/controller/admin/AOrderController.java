package com.springboot.ecommerce.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.entity.Status;
import com.springboot.ecommerce.service.IOrderService;
import com.springboot.ecommerce.service.IOrderStatusService;

@Controller
@RequestMapping("/admin/orders")
public class AOrderController {

	@Autowired
	private IOrderService service;

	@Autowired
	private IOrderStatusService statusService;

	@GetMapping("")
	public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@RequestParam(value = "sort", required = false, defaultValue = "createAt") String sort,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "accountId", required = false, defaultValue = "-1") Long accountId,
			@RequestParam(value = "statusId", required = false, defaultValue = "-1") Long statusId,
			@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
		Page<Order> response = service.paginated(page, limit, sort, order, accountId, statusId, search);
		System.out.println(accountId == null ? "null" : "OK");
		this.setPaginated(response, page, limit, sort, order, accountId, statusId, search, model);
		model.addAttribute("endPoint", "orders");
		return "page/admin/order/list";
	}

	private void setPaginated(Page<Order> response, Integer page, Integer limit, String sort, String order,
			Long accountId, Long statusId, String search, Model model) {
		model.addAttribute("response", response);
		model.addAttribute("orders", response.getContent());
		model.addAttribute("totalPages", response.getTotalPages());
		model.addAttribute("totalItems", response.getTotalElements());
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("sort", sort);
		model.addAttribute("order", order);
		model.addAttribute("reveser", order.equals("asc") || order.equals("") ? "desc" : "asc");
		model.addAttribute("accountId", accountId);
		model.addAttribute("statusId", statusId);
		model.addAttribute("search", search);
	}

	@ModelAttribute("statusList")
	public List<Status> getStatusList() {
		return statusService.findAll();
	}

}
