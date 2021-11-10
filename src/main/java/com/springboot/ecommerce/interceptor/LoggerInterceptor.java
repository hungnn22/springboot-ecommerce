package com.springboot.ecommerce.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.springboot.ecommerce.entity.Account;
import com.springboot.ecommerce.service.IAccountService;
import com.springboot.ecommerce.util.SessionService;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionService session;

	@Autowired
	private IAccountService accountService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Account account = session.get("account");
		if (account == null) {
			response.sendRedirect("/login");
			return false;
		}
		return true;

	}
}
