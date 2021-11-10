package com.springboot.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ecommerce.entity.Size;
import com.springboot.ecommerce.repository.SizeRepository;
import com.springboot.ecommerce.service.ISizeService;

@Service
public class SizeService implements ISizeService {

	@Autowired
	private SizeRepository repository;

	@Override
	public List<Size> findAll() {
		return repository.findAll();
	}
}
