package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.MailModelDTO;

public interface IMailService {

	void run();

	void push(MailModelDTO mail) throws Exception;

	void pusth(String to, String sub, String body) throws Exception;

}
