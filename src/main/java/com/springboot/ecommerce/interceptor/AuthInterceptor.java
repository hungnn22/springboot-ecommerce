package com.springboot.ecommerce.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.util.SessionService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionService sesion;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Account account = sesion.get("account");
		if (account != null && account.getRole() == 1) {
			return true;
		} else {
			response.sendRedirect("/login");
			return false;
		}
	}
}
