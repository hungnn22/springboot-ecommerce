package com.springboot.ecommerce.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.service.IOrderDetailService;
import com.springboot.ecommerce.service.impl.ReportService;

@Controller
@RequestMapping("/admin/reports")
public class AReportController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private IOrderDetailService orderDetailService;

	@Autowired
	private HttpServletResponse response;

	@GetMapping("/productsold")
	public String productSold(@RequestParam(value = "time", defaultValue = "today", required = false) String time,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
			@RequestParam(value = "view", defaultValue = "", required = false) String view, Model model) {
		List<Object[]> reports = orderDetailService.getProductSoldReport(time, page, limit);
		System.out.println(reports.size());
		model.addAttribute("endPoint", "/reports/productsold");
		this.setPaginated(reports, page, limit, view, time, model);

		if (view.equals("table")) {
			return "page/admin/report/productsold-table";
		}
		return "page/admin/report/productsold";
	}

	@GetMapping("/productsold/export")
	public String handleExport(@RequestParam(value = "time", defaultValue = "today", required = false) String time) {
		System.out.println(time);
		List<Object[]> reports = orderDetailService.getProductSoldReport(time, 1, 1000);
		ReportService excel = new ReportService(reports);
		excel.export(time, response);
		return this.redirectPreviousPage();
	}

	private void setPaginated(List<Object[]> reports, Integer page, Integer limit, String view, String time,
			Model model) {
		model.addAttribute("reports", reports);
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("view", view);
		model.addAttribute("time", time);
	}

	@GetMapping("/revenue")
	public String revenue(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
			@RequestParam(value = "statusId", defaultValue = "3", required = false) Long statusId,
			@RequestParam(value = "year", defaultValue = "2021", required = false) Integer year, Model model) {
		List<Object[]> revenues = orderDetailService.getRevenue(statusId, year, page, limit);
		this.setPaginateRevenue(revenues, page, limit, statusId, year, model);
		return "page/admin/report/revenue";
	}

	private void setPaginateRevenue(List<Object[]> revenues, Integer page, Integer limit, Long statusId, Integer year,
			Model model) {
		model.addAttribute("revenues", revenues);
		model.addAttribute("page", page);
		model.addAttribute("limit", limit);
		model.addAttribute("statusId", statusId);
		model.addAttribute("year", year);

	}

	private String redirectPreviousPage() {
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}
}
