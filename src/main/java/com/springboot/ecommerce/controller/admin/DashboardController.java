package com.springboot.ecommerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.springboot.ecommerce.entity.Order;
import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.IOrderService;

@Controller
@RequestMapping("/admin")
public class DashboardController {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderDetailService orderDetailService;

	@GetMapping(value = { "", "/" })
	public String index(@RequestParam(value = "time", defaultValue = "today", required = false) String time,
			Model model) {
		List<Object[]> reports = orderDetailService.getProductSoldReport(time, 1, 5);
		model.addAttribute("reports", reports);
		return "page/admin/dashboard";
	}

	@ModelAttribute("orders")
	public List<Order> getOrders() {
		return orderService.findTop4();
	}

	@ModelAttribute("pending")
	public Long getPendingCount() {
		System.out.println(orderService.getPendingCount());
		return orderService.getPendingCount();
	}
}
