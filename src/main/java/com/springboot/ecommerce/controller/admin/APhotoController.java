package com.springboot.ecommerce.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.ecommerce.service.IPhotoService;

@Controller
@RequestMapping("/photos")
public class APhotoController {

	@Autowired
	private IPhotoService service;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/delete/")
	public String handleDelete(@RequestParam("id") Long id) {
		service.deleteById(id);
		String referrer = request.getHeader("referer").replace("http://localhost:8080", "");
		return "redirect:" + referrer;
	}
}
