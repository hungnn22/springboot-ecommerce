package com.springboot.ecommerce.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Verify;
import com.springboot.ecommerce.repository.VerifyRepository;
import com.springboot.ecommerce.service.IVerifyService;

@Service
public class VerifyService implements IVerifyService {

	@Autowired
	private VerifyRepository repo;

	@Override
	public Verify save(Verify verify) {
		return repo.save(verify);
	}
}
